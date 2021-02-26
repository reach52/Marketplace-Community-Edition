package reach52.marketplace.community.fragments.follow_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.follow_up.AllFollowUpAdapter
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.followup_mapper.FollowUpMapper
import reach52.marketplace.community.viewmodels.FollowUpViewModel
import kotlinx.android.synthetic.main.fragment_follow_up_today_lists.view.*
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class FollowUpTodayListsFragment : Fragment() {

    private val followUpViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[FollowUpViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    private val allFollowUpsAdapter = AllFollowUpAdapter { id ->
        followUpViewModel.selectedFollowId.value = id
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, FollowUpDetailsFragment())
                .commit()
    }

    private val followUpQuery by lazy {
        DispergoDatabase.allFollowUpsView(context!!).createQuery()
                .apply {
                    startKey = ZonedDateTime
                            .now(ZoneOffset.UTC)
                            .plusHours(8)
                            .withHour(0)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0)
                    endKey = ZonedDateTime
                            .now(ZoneOffset.UTC)
                            .plusHours(8)
                            .withHour(0)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0)
                }
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
                if (event.source == this) {
                    factory.fnEnumerator = {
                        event.rows
                    }
                    followUp.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        followUp.observe(viewLifecycleOwner, Observer { it ->
            allFollowUpsAdapter.submitList(it.sortedBy { it.record.dateFollowUp })
        })

        return inflater.inflate(R.layout.fragment_follow_up_today_lists, container, false).apply {
            todayFollowUpsRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = allFollowUpsAdapter
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

}
