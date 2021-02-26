package reach52.marketplace.community.fragments.follow_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.follow_up.AllFollowUpAdapter
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.followup_mapper.FollowUpMapper
import reach52.marketplace.community.viewmodels.FollowUpViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_follow_up_lists.view.*

class FollowUpListsFragment : Fragment() {

    private val disposable = CompositeDisposable()

    private val followUpViewModel by lazy {
        activity?.run{
            ViewModelProvider(this)[FollowUpViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    private val allFollowUpAdapter = AllFollowUpAdapter{ id ->
        followUpViewModel.selectedFollowId.value = id
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, FollowUpDetailsFragment())
                .commit()
    }

    private val followUpQuery by lazy {
        DispergoDatabase.allFollowUpsView(context!!).createQuery()
    }

    private val residentFollowUpQuery by lazy {
        DispergoDatabase.residentFollowUpsView(context!!).createQuery()
    }

    private val followUpLiveQuery by lazy {
        followUpQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(followUpQuery::run, FollowUpMapper())
    }

    private val followUp by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        followUpLiveQuery.apply {
            addChangeListener {event ->
                if(event.source == this) {
                    factory.fnEnumerator = {event.rows}
                    followUp.value?.dataSource?.invalidate()
                }
            }
            start()
        }

        disposable.add(Search.query.subscribe({
            residentFollowUpQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
            }
            factory.fnEnumerator = residentFollowUpQuery::run
            followUp.value?.dataSource?.invalidate()
        },{
            throw NotImplementedError()
        }))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        followUp.observe(viewLifecycleOwner, Observer { it ->
            allFollowUpAdapter.submitList(it?.sortedByDescending { it?.record?.dateFollowUp })
        })

        return inflater.inflate(R.layout.fragment_follow_up_lists, container, false).apply {
            allFollowUpsRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = allFollowUpAdapter
            }
        }
    }

}
