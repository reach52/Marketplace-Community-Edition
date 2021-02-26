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
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.follow_up.FollowUpAdapter
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.followup_mapper.FollowUpMapper
import reach52.marketplace.community.viewmodels.FollowUpViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_follow_ups.view.*

class FollowUpsFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val residentLogisticViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    private val followUpViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[FollowUpViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    private val followUpAdapter = FollowUpAdapter {id ->
        followUpViewModel.selectedFollowId.value = id
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, FollowUpDetailsFragment())
                .addToBackStack(null)
                .commit()
    }

    private val followUpQuery by lazy {
        DispergoDatabase.toFollowUpsView(context!!).createQuery()
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

        if(residentLogisticViewModel.isLogisticResident.value == true){
            followUpQuery.apply {
                startKey = followUpViewModel.selectedLogisticResident.value?.residentId
                endKey = followUpViewModel.selectedLogisticResident.value?.residentId
            }
        } else {
            followUpQuery.apply {
                startKey = followUpViewModel.selectedResident.value?.id
                endKey = followUpViewModel.selectedResident.value?.id
            }
        }

        followUpLiveQuery.apply {
            addChangeListener { event ->
                if(event.source == this) {
                    factory.fnEnumerator = {event.rows}
                    followUp.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        followUp.observe(viewLifecycleOwner, Observer { it ->
            followUpAdapter.submitList(it?.sortedBy { it?.record?.dateFollowUp })
        })

        return inflater.inflate(R.layout.fragment_follow_ups, container, false).apply {

//            Auth.currentSession(context).map { session ->
//                // this will check the role of the login user first before emitting the document
//                if(session.user.roles.contains(AppUser.ROLE_SELF_SIGNPUP)) {
//                    followUpNotAvailableTxt.visibility = View.VISIBLE
//                    floatingActionButton.visibility = View.GONE
//                }
//            }

            followUpRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = followUpAdapter
            }

            disposables.add(floatingActionButton.clicks().subscribe{
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, AddFollowUpFragment())
                        .addToBackStack(null)
                        .commit()
            })
        }
    }

}
