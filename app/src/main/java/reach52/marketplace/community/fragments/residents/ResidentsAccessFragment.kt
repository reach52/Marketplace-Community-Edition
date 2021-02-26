package reach52.marketplace.community.fragments.residents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import arrow.core.Option
import arrow.core.Some
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.ResidentDatabase
import reach52.marketplace.community.persistence.ResidentMapper
import reach52.marketplace.community.resident.view.ResidentDetailsFragment
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import reach52.marketplace.community.viewmodels.OrderViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_residents_access.view.*
import kotlinx.android.synthetic.main.resident_details_fragment.view.*

@ExperimentalUnsignedTypes
class ResidentsAccessFragment : Fragment() {
    private val residentAdapter = ResidentAdapter()
    private val disposables = CompositeDisposable()

    private val residentsQuery by lazy {
        ResidentDatabase.residentsView(context!!).createQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(residentsQuery::run, ResidentMapper())
    }

    private val residents by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    private lateinit var mModel: Option<OrderViewModel>

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val residentLogisticViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mModel = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java]
        }

        disposables.add(residentAdapter.selectedResident.subscribe({
            mModel.map { model -> model.selectedResident = Some(it) }
            insuranceViewModel.selectedResident.value = it
            residentLogisticViewModel.isLogisticResident.value = false

            parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ResidentDetailsFragment())
                    .commit()
        }, {
            Log.w("ResidentsFragment", "selectedResidentId", it)
        }))

        disposables.add(Search.query.subscribe({
            residentsQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
            }
            factory.fnEnumerator = residentsQuery::run
            residents.value?.dataSource?.invalidate()
        }, {
            throw NotImplementedError()
        }))

        residents.observe(this, Observer { residentAdapter.submitList(it) })

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_residents_access, container, false)
                .apply {
//                    Auth.currentSession(context).map { session ->
//                        // this will check the role of the login user first before emitting the document
//                        Log.d("ryann", "role = ${session.user}")
//                        if(session.user.roles.contains(AppUser.ROLE_SELF_SIGNPUP)) {
//                            residentNotAvailableTxt.visibility = View.VISIBLE
//                        }
//                    }
                    residentsView.apply {
                        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                        adapter = residentAdapter
                    }
                }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private class ResidentAdapter :
            PagedListAdapter<Entity<Resident>, ResidentAdapter.ResidentViewHolder>(diffCallback()) {
        private val emitter: PublishSubject<Resident> = PublishSubject.create()
        val selectedResident: Observable<Resident> = emitter

        companion object {
            fun diffCallback() = object : DiffUtil.ItemCallback<Entity<Resident>>() {
                override fun areItemsTheSame(a: Entity<Resident>, b: Entity<Resident>): Boolean =
                        a.id == b.id

                override fun areContentsTheSame(a: Entity<Resident>, b: Entity<Resident>): Boolean =
                        a.record == b.record
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentViewHolder {
            return ResidentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.x_logistic_resident_list_item,
                            parent,
                            false
                    )
            )
        }

        override fun onBindViewHolder(holder: ResidentViewHolder, position: Int) {
            holder.apply {
                getItem(position)?.let { entity ->
                    setData(entity.record)
                    itemView.setOnClickListener {
                        emitter.onNext(entity.record)
                    }

                }
            }
        }

        inner class ResidentViewHolder(private val view: View) :
                androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
            fun setData(resident: Resident) {
                view.residentNameTextView.text = resident.name
                view.residentAgeTextView.text = resident.age.toString()
                view.residentSexTextView.text = resident.gender
                view.residentAddressTextView.text = resident.address.text
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = true
    }
}