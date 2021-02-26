package reach52.marketplace.community.resident.view

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class NewResidentActivity : BaseActivity() {

	companion object {
		val KEY_UPDATE = "update"
		val KEY_RESIDENT_ID = "id"
	}

	private lateinit var vm: ResidentViewModel

	private val addResidentFragment by lazy {
		AddResidentFragment()
	}
	private val insuranceProfileFragment by lazy {
		NewResidentInsuranceProfileFragment()
	}
	private val healthProfileFragment by lazy {
		NewResidentHealthProfileFragment()
	}
	private val previewProfileFragment by lazy{
		ResidentPreviewFragment()
	}
	private val serviceInterestsFragment by lazy{
		NewResidentServiceInterestsFragment()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_resident)
		setSupportActionBar(toolbar)

		vm = ViewModelProvider(this)[ResidentViewModel::class.java]
		vm.update = intent.getBooleanExtra(KEY_UPDATE, false)

		if (vm.update) {

			val id = intent.getStringExtra(KEY_RESIDENT_ID)
			dispo.add(vm.loadResidentForId(this, id).subscribe ({
				gotoFragment(addResidentFragment, emptyStack = true)
			},{

			}))

		} else {
			gotoFragment(addResidentFragment, emptyStack = true)
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

	override fun onBackPressed() {
		if (supportFragmentManager.fragments.size > 1)
			supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1].onResume()
		super.onBackPressed()
	}

	fun gotoInsuranceProfile() {
		hideKeyboard(fragment_container)
		gotoFragment(insuranceProfileFragment, addToBackStack = true)
	}

	fun gotoHealthProfile() {
		gotoFragment(healthProfileFragment, addToBackStack = true)
	}

	fun gotoServiceInterests(){
		gotoFragment(serviceInterestsFragment, addToBackStack = true)
	}

	fun gotoPreviewProfile(){
		gotoFragment(previewProfileFragment, addToBackStack = true)
	}

}