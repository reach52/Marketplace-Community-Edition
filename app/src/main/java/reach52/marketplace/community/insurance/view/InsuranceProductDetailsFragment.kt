package reach52.marketplace.community.insurance.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.aqm.view.FormActivity
import reach52.marketplace.community.databinding.FragmentInsuranceProductDetailsBinding

import reach52.marketplace.community.insurance.adapter.BenefitsAdapter
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel

class InsuranceProductDetailsFragment : Fragment() {

	val REQUEST_AQM = 1019
	private lateinit var vm: InsurancePurchaseViewModel
	lateinit var vmr: ResidentViewModel
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		val b = FragmentInsuranceProductDetailsBinding.inflate(inflater)
		vm = ViewModelProvider(activity as InsurancePurchaseActivity)[InsurancePurchaseViewModel::class.java]

		vmr = ViewModelProvider(activity as InsurancePurchaseActivity)[ResidentViewModel::class.java]
		b.product = vm.selectedInsuranceProduct
		b.benefitsList.adapter = BenefitsAdapter(context!!, vm.selectedInsuranceProduct.benefits)
		val builder = StringBuilder()
		vm.selectedInsuranceProduct.generalExclusions.forEach {
			builder.append(it).append("\n\n")
		}
		b.generalExclusions.text = builder.toString()

		b.proceedBtn.setOnClickListener {

			// if product has questions url, load AQM
			val url = vm.selectedInsuranceProduct.questionsURL
			if(url.isNullOrEmpty()) {
				(activity as InsurancePurchaseActivity).gotoConstructList()
			} else {
				(activity as InsurancePurchaseActivity).dispatchAQMIntent(REQUEST_AQM, url)
			}
		}
		return b.root
	}

	override fun onResume() {
		super.onResume()
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.insurance_products_details)
	}

	//click on home button 16-12-2020
	/*override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			R.id.home_action -> {

				try {
					(activity as InsurancePurchaseActivity).startActivity(Intent((activity as InsurancePurchaseActivity), ResidentDetailsActivity::class.java).apply {
						this.putExtra(ResidentDetailsActivity.KEY_ID, vmr.residentId)
					})
				}catch(e : java.lang.Exception)
				{
					System.out.println("onclick home= "+vmr.residentId+" "+e.message)
				}


				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}*/
	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.home_action).let {
			it.isVisible = true

			it.setOnMenuItemClickListener {
				try {

					(activity as InsurancePurchaseActivity).gotoOrderPreviewhome(context!!)
				} catch (e: java.lang.Exception) {
					System.out.println("onclick home= "  + e.message)
				}
				true
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == REQUEST_AQM) {

			if(resultCode == RESULT_OK) {
				vm.answers = data!!.getSerializableExtra(FormActivity.KEY_ANSWERS) as HashMap<String, String>
				vm.saveOrderRequest(context!!).subscribe {
					Toast.makeText(context!!, getString(R.string.purchase_requested), Toast.LENGTH_SHORT).show()
					activity!!.finish()
				}
			}

		}

	}



}