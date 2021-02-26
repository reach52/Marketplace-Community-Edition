package reach52.marketplace.community.medicine.view

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.adapters.SwipeHelper
import reach52.marketplace.community.databinding.LayoutPurchaseCompleteDialogBinding
import reach52.marketplace.community.databinding.OrderCartFragmentBinding
import reach52.marketplace.community.extensions.utils.getCurrencyString
import reach52.marketplace.community.medicine.adapter.SelectedMedicinesAdapter
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel
import reach52.marketplace.community.medicine.viewmodel.MedicineViewModel
import io.reactivex.disposables.CompositeDisposable


@ExperimentalUnsignedTypes
class OrderCartFragment : Fragment() {
	private lateinit var vmed: MedicineViewModel
	private val disposables = CompositeDisposable()
	private lateinit var vm: MedicationPurchaseViewModel
	private lateinit var b: OrderCartFragmentBinding
	var textCartItemCount: TextView? = null
	var mCartItemCount = 10
	lateinit var adapter: SelectedMedicinesAdapter
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		//(activity as MedicationPurchaseActivity).supportActionBar?.title = getString(R.string.medications)

		val activity = (activity as MedicationPurchaseActivity)
		vm = ViewModelProvider(activity)[MedicationPurchaseViewModel::class.java]
		vmed = ViewModelProvider(activity as BaseActivity)[MedicineViewModel::class.java]


		if (vm.selectedMedicines.value!!.isNotEmpty()) {
			vm.selectedMedicines.value!!.clear()
		}

		b = OrderCartFragmentBinding.inflate(inflater, container, false)
		b.vm = vm
		b.residentAgeTextView.text = vm.resident.getAgeString()

		b.addMedicationButton.setOnClickListener {
			activity.goToMedicineListFragment()
		}

		val confirmationDialog = AlertDialog.Builder(context!!)
				.setMessage(getString(R.string.confirm_this_purchase))
				.setPositiveButton(R.string.yes) { dialog, _ ->
					b.checkoutButton.isEnabled = false
					vm.saveOrder(context!!).subscribe({
						dialog.dismiss()
						showOrderPlaceDialog()
						// Toast.makeText(activity, R.string.order_created, Toast.LENGTH_SHORT).show()
					}, {
						b.checkoutButton.isEnabled = true
						Toast.makeText(context!!, it.message, Toast.LENGTH_SHORT).show()
					})
				}
				.setNegativeButton(R.string.no) { dialog, _ ->
					dialog.dismiss()
				}
				.create()

		b.checkoutButton.setOnClickListener {

			val req = vm.checkForPrescriptionRequired()
			if (req) {

				try {
					vm.validatePrescription()
					confirmationDialog.show()
				} catch (e: Exception) {
					when (e) {
						is MedicationPurchaseViewModel.PrescriptionNumberMissingException,
						is MedicationPurchaseViewModel.PrescriptionImageMissingException -> {
							activity.goToPrescriptionFragment()
						}
					}
				}

			} else {
				confirmationDialog.show()
			}

		}
		b.checkoutButton.isEnabled = false

		b.selectedMedicationsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
		val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(b.selectedMedicationsRecyclerView) {
			override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
				var buttons = listOf<UnderlayButton>()
				val deleteButton = deleteButton(position)
				buttons = listOf(deleteButton)
				/*	//val markAsUnreadButton = markAsUnreadButton(position)
					//	val archiveButton = archiveButton(position)
					when (position) {
						//1 -> buttons = listOf(deleteButton)
						//2 -> buttons = listOf(deleteButton, markAsUnreadButton)
						//3 -> buttons = listOf(deleteButton, markAsUnreadButton, archiveButton)
						else -> Unit
					}*/
				return buttons
			}
		})

		itemTouchHelper.attachToRecyclerView(b.selectedMedicationsRecyclerView)

		vm.selectedMedicines.observe(activity) {
			//val items = ArrayList(it.sortedWith(compareBy { it.medicine.supplierCode }))
			// Group All products by supplier
			adapter = SelectedMedicinesAdapter(context!!, it) {

				vm.calculatePricing()
				updatePricingUI()
				vm.selectedMedCount.postValue(it.size)

			}
			b.selectedMedicationsRecyclerView.adapter = adapter

		}
		return b.root
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}


	private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
		return SwipeHelper.UnderlayButton(
				context!!,
				"Delete",
				14.0f,
				android.R.color.holo_red_light,
				object : SwipeHelper.UnderlayButtonClickListener {
					override fun onClick() {
						//toast()
						Log.d("DeleteCheck", "onClick: " + position)
						adapter.removeMedication(position)
					}
				})
	}


	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.action_cart).isVisible = true
		//MedicationPurchaseActivity.selecteMedsCount
		if (vm.selectedMedicines.value!!.size == 0) {
			vm.selectedMedicines.value!!.clear()
			menu.findItem(R.id.action_cart).isVisible = false

		}

	}

	override fun onResume() {
		super.onResume()
		(activity as MedicationPurchaseActivity).supportActionBar?.title = getString(R.string.medications)
	}


	fun updatePricingUI() {

		b.subTotalTextView.text = getCurrencyString(vm.subTotal.toString(),vm.currency)
		b.feeTextView.text = getCurrencyString(vm.deliveryFee.toString(),vm.currency)
		b.taxTextView.text = getCurrencyString(vm.taxAmt.toString(), vm.currency)
		b.discountTextView.text = getCurrencyString("- ${vm.discountAmt}", vm.currency)
		b.totalTextView.text = getCurrencyString(vm.total.toString(), vm.currency)
		b.subTotalTextView.text = getCurrencyString(vm.subTotal.toString(), vm.currency)
		b.feeTextView.text = getCurrencyString(vm.deliveryFee.toString() , vm.currency)
		b.taxTextView.text = getCurrencyString(vm.taxAmt.toString(), vm.currency)
		b.discountTextView.text = getCurrencyString("- ${vm.discountAmt}", vm.currency)
		b.totalTextView.text = getCurrencyString(vm.total.toString(), vm.currency)

		b.checkoutButton.isEnabled = !vm.selectedMedicines.value!!.isEmpty()
	}


	fun showOrderPlaceDialog() {
		val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
		val binding: LayoutPurchaseCompleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_purchase_complete_dialog, null, false)
		dialogBuilder.setView(binding.root)
		val alertDialog: AlertDialog = dialogBuilder.create()
		binding.tvPurchaseCompleted.text = getString(R.string.order_created)
		alertDialog.show()
		Handler().postDelayed({
			alertDialog.dismiss()
			activity!!.finish()
		}, 2000)

	}

}




