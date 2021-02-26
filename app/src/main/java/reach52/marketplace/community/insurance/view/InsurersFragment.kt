package reach52.marketplace.community.insurance.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentInsurersBinding
import reach52.marketplace.community.insurance.adapter.InsurerListAdapter
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel


class InsurersFragment : Fragment() {

	private lateinit var vm: InsurancePurchaseViewModel
	lateinit var vmr: ResidentViewModel


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {

		val activity = activity as InsurancePurchaseActivity
		vm = ViewModelProvider(activity)[InsurancePurchaseViewModel::class.java]
		vmr = ViewModelProvider(activity)[ResidentViewModel::class.java]
		val b = FragmentInsurersBinding.inflate(inflater, container, false)
		vm.insurers.observe(activity, { list ->

			b.insurersList.layoutManager = GridLayoutManager(context!!, 2)
			b.insurersList.adapter = InsurerListAdapter(context!!, list) {
				vm.selectedInsurer = it
				activity.gotoInsuranceProducts()
			}
		})
		vm.loadInsurers(context!!)
		return b.root
	}

	//click on home button 16-12-2020
	/*override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			R.id.home_action -> {

				try {
					activity?.startActivity(Intent(activity, ResidentDetailsActivity::class.java).apply {
						this.putExtra(ResidentDetailsActivity.KEY_ID, vmr.residentId)
					})
				}catch(e : Exception)
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
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.insurers)
	}
}