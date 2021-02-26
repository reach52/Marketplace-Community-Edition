package reach52.marketplace.community.resident.view

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.CountryCode
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import reach52.marketplace.community.viewmodels.FollowUpViewModel
import reach52.marketplace.community.viewmodels.OrderViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.resident_details_fragment.view.*

@Suppress("UNREACHABLE_CODE")
@ExperimentalUnsignedTypes
class ResidentDetailsFragment : androidx.fragment.app.Fragment() {
	private var selectedItem = 0
	private lateinit var model: Option<OrderViewModel>
	private val disposables = CompositeDisposable()

	private val residentLogisticViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	private val followUpViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[FollowUpViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

		model = Option.fromNullable(activity).map {
			ViewModelProvider(it)[OrderViewModel::class.java]
		}
	}

	private lateinit var vm: ResidentViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		vm = ViewModelProvider(activity as FragmentActivity)[ResidentViewModel::class.java]

		return inflater.inflate(R.layout.resident_details_fragment, container, false).apply {
			(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.resident_details)
			hideActionBar()

//			engagementBtn.setOnClickListener{
//
//				(activity as AppCompatActivity).startActivityForResult(Intent(context, FormActivity::class.java).apply{
//					putExtra(FormActivity.KEY_URL, "test")
//				}, 111)
//
//			}

			parentFragmentManager
					.beginTransaction()
					.addToBackStack("residentDetails")
					.commit()

			this.isFocusableInTouchMode = true
			this.requestFocus()
			this.setOnKeyListener { _, keyCode, _ ->
				if (residentLogisticViewModel.residentCounter.value == 1) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
//						Auth.currentSession(context).map { session ->
							// this will check the role of the login user first before emitting the document
//							if (session.user.roles.contains(AppUser.ROLE_SELF_SIGNPUP)) {
								parentFragmentManager
										.beginTransaction()
										.replace(R.id.fragment_container, LogisticResidentsListFragment())
										.commit()
//							} else {
//								parentFragmentManager
//										.beginTransaction()
//										.replace(R.id.fragment_container, ResidentsFragment())
//										.commit()
//							}
//						}
					}
				} else {
					residentLogisticViewModel.residentCounter.value = 1
				}
				false
			}

			if (vm.isLogisticResident) {

				val resident = vm.resident
				residentAddressTextView.text = resident.addressLine
				residentAgeTextView.text = resident.getAgeString()
				residentNameTextView.text = "${resident.firstName}, ${resident.lastName}"
				residentSexTextView.text = resident.gender
				updateResidentButton.visibility = VISIBLE
				followUpViewModel.selectedLogisticResident.value = resident.toLogisticResident()
				followUpViewModel.isLogisticResident.value = true

//                model.flatMap {
//                    it.selectedLogisticResident
//                }.map {
//                    residentAddressTextView.text = it.address
//                    residentAgeTextView.text = it.age.toString()
//                    residentNameTextView.text = it.name
//                    residentSexTextView.text = it.gender
//                    updateResidentButton.visibility = VISIBLE
//                    residentLogisticViewModel.selectedLogisticResident.value = it
//                    followUpViewModel.selectedLogisticResident.value = it
//                    followUpViewModel.isLogisticResident.value = true
//                }
			} else {
				model.flatMap {
					it.selectedResident
				}.map {
					residentAddressTextView.text = it.address.text
					residentAgeTextView.text = it.age.toString()
					residentNameTextView.text = it.name
					residentSexTextView.text = it.gender
					followUpViewModel.selectedResident.value = it
					followUpViewModel.isLogisticResident.value = false
				}
			}

//            residentNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//            if (selectedItem != 0){
//                residentNavigation.selectedItemId = selectedItem
//            }
//            else{
//                residentNavigation.selectedItemId = R.id.nav_medication
//            }

			if (CountryManager.getCountryCode() == CountryCode.IND) {
//                updateResidentButton.visibility = View.INVISIBLE
			}

			updateResidentButton.setOnClickListener {
//                residentLogisticViewModel.isLogisticResidentUpdate.value = true
//                parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, AddResidentFragment())
//                        .commit()
//                residentLogisticViewModel.residentCounter.value = 0

				val intent = Intent(activity, NewResidentActivity::class.java).apply {
					putExtra(NewResidentActivity.KEY_UPDATE, true)
					putExtra(NewResidentActivity.KEY_RESIDENT_ID, vm.resident.id)
				}
				startActivity(intent)
			}

		}
	}

//    override fun onPrepareOptionsMenu(menu: Menu) {
//        super.onPrepareOptionsMenu(menu)
//        menu.findItem(R.id.app_bar_search).isVisible = false
//    }

	override fun onDestroy() {
		disposables.clear()
		super.onDestroy()
	}

//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.nav_medication -> {
//                replaceFragment(MedicationFragment())
//                selectedItem = item.itemId
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_insurance -> {
//                replaceFragment(PolicyOwnerFragment())
//                selectedItem = item.itemId
//                return@OnNavigationItemSelectedListener true
////                replaceFragment(InsuredFragment())
//            }
//            R.id.nav_consumer_health -> {
//                replaceFragment(ConsumerHealthFragment())
//                selectedItem = item.itemId
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_follow_up -> {
//                replaceFragment(FollowUpsFragment())
//                selectedItem = item.itemId
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }

	private fun replaceFragment(fragment: Fragment) {
		parentFragmentManager
				.beginTransaction()
				.replace(R.id.residentMenuContainer, fragment)
				.commit()
	}

	private fun hideActionBar() {
		val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
		toolbar.navigationIcon = null
	}

}