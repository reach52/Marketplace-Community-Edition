package reach52.marketplace.community.insurance.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.aqm.utils.C
import reach52.marketplace.community.aqm.view.FormActivity
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.view.ResidentDetailsActivity
import kotlinx.android.synthetic.main.app_bar_main.*


class InsurancePurchaseActivity : BaseActivity() {

	companion object {
		const val KEY_RESIDENT_ID = "res_id"
	}
	private lateinit var menu: Menu
	private lateinit var id: String
	private lateinit var vm: InsurancePurchaseViewModel
	private val consentFragment by lazy { InsuranceConsentFragment() }
	private val insurersFragment by lazy { InsurersFragment() }
	private val insuranceProductsFragment by lazy { InsuranceProductsFragment() }
	private val insuranceProductsDetailsFragment by lazy { InsuranceProductDetailsFragment() }
	private val constructsFragment by lazy { ConstructsFragment() }
	private val membersFragment by lazy { MembersFragment() }
	private val premiumFragment by lazy { PremiumFragment() }
	private val beneficiariesFragment by lazy { BeneficiariesFragment() }
	private val idUploadFragment by lazy { IDUploadFragment() }
	private val orderPreviewFragment by lazy { OrderPreviewFragment() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_insurance_purchase)
		setSupportActionBar(toolbar)

		 id = intent.getStringExtra(KEY_RESIDENT_ID)

		vm = ViewModelProvider(this)[InsurancePurchaseViewModel::class.java]
		dispo.add(vm.loadResident(this, id).subscribe {
			gotoFragment(consentFragment, true)
		})

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == idUploadFragment.REQUEST_IMAGE_CAPTURE) {
			idUploadFragment.onActivityResult(requestCode, resultCode, data)
		}

		if (requestCode == insuranceProductsDetailsFragment.REQUEST_AQM) {
			insuranceProductsDetailsFragment.onActivityResult(requestCode, resultCode, data)
		}

	}




	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.insurance_purchase, menu)
		this.menu = menu;


	/*	menu.findItem(R.id.action_homee).actionView.setOnClickListener {
			try {
				System.out.println("rclick1= ")
			}catch (e: java.lang.Exception)
			{
				e.message
			}
		}*/
		return true
	}

	override fun onPrepareOptionsMenu(menu: Menu): Boolean {
		super.onPrepareOptionsMenu(menu)
		val homeitem = menu.findItem(R.id.home_action)
		homeitem.isVisible=true

		return super.onPrepareOptionsMenu(menu)
	}


	override fun onBackPressed() {
		if (supportFragmentManager.fragments.size > 1) {
			supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1].onResume()
		}
			super.onBackPressed()
	}
	// click on toolbar backpress arrow
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
			/*R.id.home_action -> {

				try {
					this.startActivity(Intent(this, ResidentDetailsActivity::class.java).apply {
						this.putExtra(ResidentDetailsActivity.KEY_ID, id)
					})
				}catch(e : Exception)
				{
					System.out.println("onclick home= "+id+" "+e.message)
				}


				return true
			}*/
		}
		return super.onOptionsItemSelected(item)
	}


	fun dispatchTakePictureIntent(requestCode: Int) {
		val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		if (takePictureIntent.resolveActivity(packageManager) != null) {

			val uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.FileProvider", vm.idImageFile)
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
			startActivityForResult(takePictureIntent, requestCode)

		}
	}


	fun dispatchAQMIntent(requestCode: Int, url: String) {
		val intent = Intent(this, FormActivity::class.java)
		intent.putExtra(FormActivity.KEY_URL, url)
		intent.putExtra(FormActivity.KEY_ANS_FORMAT, C.ANS_FORMAT_TEXT)
		startActivityForResult(intent, requestCode)
	}

	fun gotoInsurers() {
		gotoFragment(insurersFragment, false, true)
	}

	fun gotoInsuranceProducts() {
		gotoFragment(insuranceProductsFragment, false, true)
	}

	fun gotoInsuranceProductDetails() {
		gotoFragment(insuranceProductsDetailsFragment, false, true)
	}

	fun gotoConstructList() {
		gotoFragment(constructsFragment, false, true)
	}

	fun gotoMembersList() {
		gotoFragment(membersFragment, false, true)
	}

	fun gotoPremiumFragment() {
		gotoFragment(premiumFragment, false, true)
	}

	fun gotoBeneficiariesFragment() {
		gotoFragment(beneficiariesFragment, false, true)
	}

	fun gotoIDUploadFragment() {
		gotoFragment(idUploadFragment, false, true)
	}

	fun gotoOrderPreview() {
		gotoFragment(orderPreviewFragment, false, true)
	}


	fun gotoOrderPreviewhome(context: Context) {
		try {
			context.startActivity(Intent(context, ResidentDetailsActivity::class.java).apply {
				this.putExtra(ResidentDetailsActivity.KEY_ID, id)
				this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						or Intent.FLAG_ACTIVITY_CLEAR_TOP)
				finish()
			})

		}catch (e: Exception)
		{
			System.out.println("onclick home= "  + e.message)
		}
	}


}