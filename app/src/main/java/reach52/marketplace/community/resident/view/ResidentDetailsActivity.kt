package reach52.marketplace.community.resident.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.aqm.view.FormActivity
import reach52.marketplace.community.fragments.consumer_health.ConsumerHealthFragment
import reach52.marketplace.community.fragments.follow_up.FollowUpsFragment
import reach52.marketplace.community.persistence.Api
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.activity_resident_details.*
import kotlinx.android.synthetic.main.activity_resident_details.toolbar
import kotlinx.android.synthetic.main.app_bar_main.*
import org.threeten.bp.format.DateTimeFormatter

class ResidentDetailsActivity : BaseActivity() {

	companion object {
		const val KEY_ID = "id"
		const val KEY_IS_LOGISTIC = "is_logi"
		const val RESULT_UPDATE = 1
	}

	private var selectedItem = 0
	private lateinit var vm: ResidentViewModel
	private lateinit var mToolbar: Toolbar

	private val medicationFragment by lazy { MedicationOrdersFragment() }
	private val policyOrderFragment by lazy { PolicyOrdersFragment() }
	private val consumerHealthFragment by lazy { ConsumerHealthFragment() }
	private val followupFragment by lazy { FollowUpsFragment() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_resident_details)
		setSupportActionBar(toolbar)



		vm = ViewModelProvider(this)[ResidentViewModel::class.java]
		vm.isLogisticResident = intent.getBooleanExtra(KEY_IS_LOGISTIC, true)
		vm.residentId = intent.getStringExtra(KEY_ID)

		dispo.add(
				vm.loadResidentForId(this, vm.residentId).subscribe(
						{
							if (vm.isLogisticResident) {
								updateDetailsUI(vm.resident)
							}
						},
						{
							Log.d("taaag", "message = $it")
						}
				)
		)

		residentNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
		if (selectedItem != 0) {
			residentNavigation.selectedItemId = selectedItem
		} else {
			residentNavigation.selectedItemId = R.id.nav_medication
		}

		engagementBtn.setOnClickListener {
			startActivityForResult(Intent(this, FormActivity::class.java).apply{
				putExtra(FormActivity.KEY_URL, Api.reEngagementQuestionUrl)
			}, 111)
		}

	}

	private fun updateDetailsUI(resident: Resident) {
		residentAddressTextView.text = resident.addressLine
		residentAgeTextView.text = resident.getAgeString()
		residentNameTextView.text = resident.fullName()
		residentSexTextView.text = resident.gender


		// add some field 15-12-2020
		residentDOBTextView.text = resident.dob.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
		residentContTextView.text =resident.phone

		updateResidentButton.visibility = View.VISIBLE

		updateResidentButton.setOnClickListener {

			val intent = Intent(this, NewResidentActivity::class.java).apply {
				putExtra(NewResidentActivity.KEY_UPDATE, true)
				putExtra(NewResidentActivity.KEY_RESIDENT_ID, resident.id)
			}
			startActivityForResult(intent, RESULT_UPDATE)
		}



	}

	@SuppressLint("CheckResult")
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RESULT_UPDATE) {
			dispo.add(
					vm.loadResidentForId(this, vm.residentId).subscribe(
							{
								updateDetailsUI(it)
							},
							{

							}
					)
			)
		}

		if(requestCode == 111){

			if(resultCode == Activity.RESULT_OK) {
				val answers = data!!.getSerializableExtra(FormActivity.KEY_ANSWERS) as Map<String, String>

				vm.saveEngagement(this, answers).subscribe {

				}
			}

		}

	}

	// click on toolbar backpress arrow
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}
//	private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//		Toast.makeText(this, "Temporarily disabled.", Toast.LENGTH_SHORT).show()
//		false
//	}

	private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		when (item.itemId) {
			R.id.nav_medication -> {
				gotoFragment(medicationFragment)
				selectedItem = item.itemId
				return@OnNavigationItemSelectedListener true
			}
			R.id.nav_insurance -> {
				gotoFragment(policyOrderFragment)
				selectedItem = item.itemId
				return@OnNavigationItemSelectedListener true
//                replaceFragment(InsuredFragment())
			}
//			R.id.nav_consumer_health -> {
//				gotoFragment(consumerHealthFragment)
//				selectedItem = item.itemId
//				return@OnNavigationItemSelectedListener true
//			}
//			R.id.nav_follow_up -> {
//				gotoFragment(followupFragment)
//				selectedItem = item.itemId
//				return@OnNavigationItemSelectedListener true
//			}
		}
		false
	}


}