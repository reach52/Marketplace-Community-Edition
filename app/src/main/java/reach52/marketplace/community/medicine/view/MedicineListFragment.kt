package reach52.marketplace.community.medicine.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.databinding.FragmentMedicineListBinding
import reach52.marketplace.community.medicine.adapter.MedicineAdapter
import reach52.marketplace.community.medicine.entity.Medicine
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel
import reach52.marketplace.community.medicine.viewmodel.MedicineViewModel


class MedicineListFragment : Fragment() {

	private lateinit var vm: MedicineViewModel
	private lateinit var vmPurchase: MedicationPurchaseViewModel
	private lateinit var listObserver: Observer<ArrayList<Medicine>>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
		vm = ViewModelProvider(activity as BaseActivity)[MedicineViewModel::class.java]
		vmPurchase = ViewModelProvider(activity as BaseActivity)[MedicationPurchaseViewModel::class.java]
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val activity = activity as MedicationPurchaseActivity

		val dialog = AlertDialog.Builder(activity)
				.setMessage(getString(R.string.loading_meds_please_wait))
				.setCancelable(false)
				.create()

		val b = FragmentMedicineListBinding.inflate(inflater, container, false)

		listObserver = Observer { list ->
			if (list.isNotEmpty()) {
				dialog.dismiss()
				b.medicinesListRecyclerView.visibility = View.VISIBLE
				b.noMeds.visibility = View.GONE
				if(context != null) {
					b.medicinesListRecyclerView.adapter = MedicineAdapter(context!!, list) { it, qty ->
						vmPurchase.addMedicine(it, qty = qty)
						activity.supportFragmentManager.popBackStack()
					}
				}
			} else {
				dialog.dismiss()
				b.medicinesListRecyclerView.visibility = View.GONE
				b.noMeds.visibility = View.VISIBLE
			}
		}

		vm.medicines.observe(activity, listObserver)

		dialog.show()
		vm.loadMedicines(context!!, vm.suplCodeList)

		return b.root
	}

	override fun onResume() {
		super.onResume()
		(activity as MedicationPurchaseActivity).supportActionBar?.title = getString(R.string.medicine_list)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		val searchItem = menu.findItem(R.id.app_bar_search)
		searchItem.isVisible = true


		menu.findItem(R.id.action_cart).actionView.setOnClickListener {
			try {
				vm.medicines.removeObserver(listObserver)
				(activity as MedicationPurchaseActivity).onBackPressed()
			} catch (e: java.lang.Exception) {
				e.message
			}
		}

		val searchView = searchItem.actionView as SearchView
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String): Boolean {

				vm.searchFilterMedicines(context!!, vm.suplCodeList, query)
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
				// vm.loadMedicines(context!!, vmPurchase.selectedSupplier)
				vm.loadMedicines(context!!, vm.suplCodeList)

				return true
			}

		})

	}

}