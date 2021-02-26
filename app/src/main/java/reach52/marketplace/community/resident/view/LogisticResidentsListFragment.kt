package reach52.marketplace.community.resident.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.home.view.MainActivity
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.resident.adapter.LogisticResidentsAdapter
import reach52.marketplace.community.resident.viewmodel.ResidentsViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_logistic_residents_list.view.*


@ExperimentalUnsignedTypes
class LogisticResidentsListFragment : Fragment() {

	private val residentLogisticViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

//	private val insuranceViewModel by lazy {
//		activity?.run {
//			ViewModelProvider(this)[InsuranceViewModel::class.java]
//		} ?: throw Exception("Invalid Activity")
//	}

//	private val residentLogisticAdapter = ResidentLogisticAdapter()

//	private lateinit var orderViewModel: Option<OrderViewModel>

	private val disposables = CompositeDisposable()

//	private val residentLogisticQuery by lazy {
//		DispergoDatabase.logisticResidentView(context!!).createQuery()
//	}

//	private val residentLiveQuery by lazy {
//		residentLogisticQuery.toLiveQuery()
//	}

//	private val factory by lazy {
//		EntityDataSourceFactory(residentLogisticQuery::run, ResidentMapper())
//	}

//	private val residents by lazy {
//		LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
//	}

	lateinit var toolbar: Toolbar
	private lateinit var ctx: Context
	private lateinit var vm: ResidentsViewModel
	private lateinit var logisticResidentsAdapter: LogisticResidentsAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

		ctx = context!!
		vm = ViewModelProvider(activity as FragmentActivity)[ResidentsViewModel::class.java]

//		orderViewModel = Option.fromNullable(activity).map {
//			ViewModelProvider(it)[OrderViewModel::class.java]
//		}

//		disposables.add(residentLogisticAdapter.selectedResident.subscribe {
//			orderViewModel.map { model -> model.selectedLogisticResident = Some(it.toLogisticResident()) }
//			insuranceViewModel.selectedLogisticResident.value = it.toLogisticResident()
//			residentLogisticViewModel.isLogisticResident.value = true
//
//			parentFragmentManager
//					.beginTransaction()
//					.replace(R.id.fragment_container, ResidentDetailsFragment())
//					.commit()
//		})

//		disposables.add(Search.query.subscribe({
//			residentLogisticQuery.apply {
//				startKey = if (it.isNotEmpty()) it else null
//				endKey = if (it.isNotEmpty()) it + "\u9999" else null
//			}
//			factory.fnEnumerator = residentLogisticQuery::run
//			residents.value?.dataSource?.invalidate()
//		}, {
//			throw NotImplementedError()
//		}))

//		residents.observe(this, Observer { residentLogisticAdapter.submitList(it) })
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {

		logisticResidentsAdapter = LogisticResidentsAdapter(context!!) {

//            orderViewModel.map { model -> model.selectedLogisticResident = Some(it.toLogisticResident()) }
//            insuranceViewModel.selectedLogisticResident.value = it.toLogisticResident()
//            residentLogisticViewModel.isLogisticResident.value = true
//
//            parentFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, ResidentDetailsFragment())
//                    .commit()

			activity!!.startActivity(Intent(ctx, ResidentDetailsActivity::class.java).apply {
				this.putExtra(ResidentDetailsActivity.KEY_ID, it.id)
			})


		}
		vm.logisticResidents.observe(activity as FragmentActivity, Observer {
			Log.v("taaag", "${it.size} residents observed")
			logisticResidentsAdapter.residentsList = it
		})
//		disposables.add(Search.query.subscribe({
//			vm.searchFilterLogisticsResidents(ctx, it)
//		}, {
//			throw NotImplementedError()
//		}))

//        residentLiveQuery.apply {
//            addChangeListener { event ->
//                if (event.source == this) {
//                    vm.loadLogisticsResidents(ctx)
//                    factory.fnEnumerator = { event.rows }
//                    residents.value?.dataSource?.invalidate()
//                }
//            }
//            start()
//        }

		return inflater.inflate(R.layout.fragment_logistic_residents_list, container, false).apply {
//			Auth.currentSession(context).map { session ->
				toolbar = activity!!.findViewById(R.id.toolbar)
				val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
				val toggle = ActionBarDrawerToggle(
						activity, drawer, toolbar, R.string.navigation_drawer_open,
						R.string.navigation_drawer_close)
				toggle.isDrawerIndicatorEnabled = true
				drawer.addDrawerListener(toggle)
				toggle.syncState()

			// code for set tootlbar design
			toolbar.setNavigationIcon(R.drawable.ic_notes);
			toolbar.setBackgroundColor(resources.getColor(R.color.textColorWhite))
			toolbar.setTitleTextColor(resources.getColor(R.color.purple))
			toolbar.setTitleTextAppearance(activity, R.style.ToolbarTheme);



			// this will check the role of the login user first before emitting the document
				parentFragmentManager
						.beginTransaction()
						.addToBackStack("residentLogistic")
						.commit()

//				if (session.user.roles.contains(AppUser.ROLE_SELF_SIGNPUP)) {
					this.isFocusableInTouchMode = true
					this.requestFocus()
					this.setOnKeyListener { _, keyCode, _ ->
						if (residentLogisticViewModel.residentCounter.value == 0) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								(activity as MainActivity).finish()
							}
						} else {
							residentLogisticViewModel.residentCounter.value = 0
						}
						false
					}
//				}
//			}

			residentsLogisticRecyclerView.apply {
				layoutManager = LinearLayoutManager(activity)
				adapter = logisticResidentsAdapter
			}

			disposables.add(floatingActionButton.clicks().subscribe {
//                Option.fromNullable(residentLogisticViewModel.selectedLogisticResident.value).map { it }.getOrElse { "" }
//                parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, AddResidentFragment())
//                        .commit()
//                residentLogisticViewModel.residentCounter.value = 1

				startActivity(Intent(context, NewResidentActivity::class.java))


			})

			// TODO remove this test code later
			if (BuildConfig.BUILD_TYPE == "debug") {
				floatingActionButton.setOnLongClickListener {
					DispergoDatabase.logisticResidentView(context!!).createQuery().toLiveQuery().run().forEach {
						val id = it.documentId
						DispergoDatabase.getInstance(context!!).getDocument(it.documentId).delete()
						Log.d("taaag", "deleted doc: $id")
					}
					true
				}
			}

			Log.v("taaag", "loading residents in LogisticResidentsListFragment.onViewCreated")
			vm.loadLogisticsResidents(ctx)
		}


	}

	@SuppressLint("WrongViewCast")
	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		val searchItem = menu.findItem(R.id.app_bar_search)

		searchItem.isVisible = true
		val searchView = searchItem.actionView as SearchView

		// code for search text color 11-12-2020
		val theTextArea = searchView.findViewById<View>(R.id.search_src_text) as SearchAutoComplete
		theTextArea.setTextColor(resources.getColor(R.color.purple))
		theTextArea.setHintTextColor(resources.getColor(R.color.purple))

		// 	// code for search clear text
		val searchClose: ImageView = searchView.findViewById<View>(R.id.search_close_btn) as ImageView
		searchClose.setColorFilter(resources.getColor(R.color.purple))



// 	// code for search clear text
		val searchClosee: ImageView = searchView.findViewById<View>(R.id.search_go_btn) as ImageView
		searchClosee.setColorFilter(resources.getColor(R.color.purple))




		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String): Boolean {

				vm.searchFilterLogisticsResidents(context!!, query)
				return true
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				return false
			}

		})
		searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
			override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
				return true
			}

			override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
				vm.loadLogisticsResidents(context!!)
				return true
			}

		})

	}

	override fun onResume() {
		super.onResume()
		(activity as BaseActivity).supportActionBar?.title = getString(R.string.residents)
	}

	override fun onDestroy() {
		disposables.dispose()
		super.onDestroy()
	}

	fun refreshResidents() {
		vm.loadLogisticsResidents(context!!)
	}

}