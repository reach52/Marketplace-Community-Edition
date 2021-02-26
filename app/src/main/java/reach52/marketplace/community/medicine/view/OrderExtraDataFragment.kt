package reach52.marketplace.community.medicine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.medicine.adapter.DiscountsListAdapter
import reach52.marketplace.community.medicine.entity.Discount
import reach52.marketplace.community.medicine.entity.Physician
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel
import kotlinx.android.synthetic.main.order_extra_data_fragment.view.*

@ExperimentalUnsignedTypes
class OrderExtraDataFragment : androidx.fragment.app.Fragment() {

	val REQUEST_IMAGE_CAPTURE = 32

	private lateinit var vm: MedicationPurchaseViewModel


	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {

		val activity = activity as MedicationPurchaseActivity
		vm = ViewModelProvider(activity)[MedicationPurchaseViewModel::class.java]

		return inflater.inflate(R.layout.order_extra_data_fragment, container, false).apply {
			(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.order_medication)


			vm.physicians.observe(activity, Observer {

				if (it.isEmpty()) {

					physician_group.visibility = View.GONE

				} else {

					physicianLicenseSpinner.adapter = ArrayAdapter(
							context!!,
							android.R.layout.simple_spinner_dropdown_item,
							it
					)
					physicianLicenseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
						override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
							val physician = parent.getItemAtPosition(position) as Physician
							vm.selectedPhysician = if (physician.id.isEmpty()) null else physician
						}

						override fun onNothingSelected(parent: AdapterView<*>?) {
							vm.selectedPhysician = null
						}

					}

				}
			})

			vm.loadPhysicians(context!!)

			vm.discounts.observe(activity, Observer {

				if (it.isEmpty()) {
					discount_group.visibility = View.GONE
				} else {

					discount_group.visibility = View.VISIBLE
					discountCodeSpinner.adapter = DiscountsListAdapter(activity, it)
					discountCodeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
						override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
							val discount = parent.getItemAtPosition(position) as Discount
							vm.selectedDiscount = if (discount.code.isEmpty()) null else discount
						}

						override fun onNothingSelected(parent: AdapterView<*>?) {
							vm.selectedDiscount = null
						}

					}
				}
			})
			vm.loadDiscounts(context)

			activity.dispo.add(discountIdNumberTextInputEditText.textChanges().subscribe({
				vm.discountNumber = it.toString()
			}, {
				throw NotImplementedError()
			}))

			saveButton.setOnClickListener {

				try {
					vm.validateDiscount()
					activity.goToSupplierFragment()
				} catch (e: Exception) {
					when (e) {
						is MedicationPurchaseViewModel.DiscountNumberMissingException -> {
							Toast.makeText(context, context.getString(R.string.add_discount_number), Toast.LENGTH_SHORT).show()
						}
					}
				}

			}

		}
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

	}
	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.action_cart).isVisible = false
	}

	override fun onResume() {
		super.onResume()
		(activity as MedicationPurchaseActivity).supportActionBar?.title = getString(R.string.order_medication)
	}
}

