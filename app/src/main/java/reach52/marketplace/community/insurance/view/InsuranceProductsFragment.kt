package reach52.marketplace.community.insurance.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.insurance.adapter.InsuranceProductListAdapter
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.fragment_insurance_products.view.*

class InsuranceProductsFragment : Fragment() {
	lateinit var vmr: ResidentViewModel
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		val activity = activity as InsurancePurchaseActivity

		val vm = ViewModelProvider(activity)[InsurancePurchaseViewModel::class.java]
		vmr = ViewModelProvider(activity)[ResidentViewModel::class.java]
		return inflater.inflate(R.layout.fragment_insurance_products, container, false).apply {

			vm.insuranceProducts.observe(activity, {

				productsList.adapter = InsuranceProductListAdapter(context, it) { product ->
					try {
						vm.isResidentEligible(product)
						vm.selectedInsuranceProduct = product
						activity.gotoInsuranceProductDetails()
					} catch (e: Exception) {
						when (e) {
							is InsurancePurchaseViewModel.AgeTooLowException -> {
								Toast.makeText(context, getString(R.string.min_age_required) + e.message, Toast.LENGTH_SHORT).show()
							}
							is InsurancePurchaseViewModel.AgeTooHighException -> {
								Toast.makeText(context, getString(R.string.max_age_is) + e.message, Toast.LENGTH_SHORT).show()
							}
							else -> {
								Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
							}
						}
					}
				}
			})

			vm.loadInsuranceProducts(context, vm.selectedInsurer)

		}
	}

	//click on home button 16-12-2020
/*	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			R.id.home_action -> {

				try {
					activity?.startActivity(Intent(activity, ResidentDetailsActivity::class.java).apply {
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

	override fun onResume() {
		super.onResume()
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.insurance_products)
	}
}