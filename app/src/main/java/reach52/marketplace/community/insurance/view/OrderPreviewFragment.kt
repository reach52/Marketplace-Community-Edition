package reach52.marketplace.community.insurance.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentOrderPreviewBinding
import reach52.marketplace.community.databinding.LayoutPurchaseCompleteDialogBinding
import reach52.marketplace.community.insurance.adapter.BeneficiaryListAdapter
import reach52.marketplace.community.insurance.adapter.MemberListAdapter
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel

class OrderPreviewFragment : Fragment() {

	private lateinit var vm: InsurancePurchaseViewModel
	lateinit var vmr: ResidentViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.save).let {
			it.isVisible = false
		}
		menu.findItem(R.id.home_action).let {
			it.isVisible = true

			it.setOnMenuItemClickListener {
				try {

					(activity as InsurancePurchaseActivity).gotoOrderPreviewhome(context!!)
				} catch (e: java.lang.Exception) {
					System.out.println("onclick home= " +  e.message)
				}
				true
			}
		}

	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		val b = FragmentOrderPreviewBinding.inflate(inflater, container, false)

		val activity = activity as InsurancePurchaseActivity
		vm = ViewModelProvider(activity)[InsurancePurchaseViewModel::class.java]
		vmr = ViewModelProvider(activity)[ResidentViewModel::class.java]
		b.vm = vm
		b.previewMembersList.adapter = MemberListAdapter(context!!, vm.selectedMembers, simplified = true)
		b.previewMembersList.setNestedScrollingEnabled(false)
		b.previewBeneficiaryList.adapter = BeneficiaryListAdapter(context!!, vm.beneficiaries, simplified = true)
		b.previewBeneficiaryList.setNestedScrollingEnabled(false)
		b.previewIdImg.setImageBitmap(BitmapFactory.decodeFile(vm.idImageFile.absolutePath))

		val confirmationDialog = AlertDialog.Builder(context!!)
				.setMessage(getString(R.string.confirm_this_purchase))
				.setPositiveButton(R.string.yes) { dialog, _ ->
					b.buyBtn.isEnabled = false
					vm.saveOrder(context!!).subscribe({
						showOrderPlaceDialog()
					}, {
						b.buyBtn.isEnabled = true
						Toast.makeText(context!!, it.message, Toast.LENGTH_SHORT).show()
					})
				}
				.setNegativeButton(R.string.no){ dialog, _ ->
					dialog.dismiss()
				}
				.create()

		b.buyBtn.setOnClickListener {

			confirmationDialog.show()

		}

		return b.root
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

	override fun onResume() {
		super.onResume()
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.order_preview)
	}

	fun showOrderPlaceDialog()
	{
		val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
		val binding: LayoutPurchaseCompleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_purchase_complete_dialog, null, false)
		dialogBuilder.setView(binding.getRoot())
		val alertDialog: AlertDialog = dialogBuilder.create()
		binding.tvPurchaseCompleted.text = getString(R.string.your_insurance_purchase_is_complete);
		alertDialog.show()
		Handler().postDelayed({
			alertDialog.dismiss()
			activity!!.finish()
		}, 2000)



	}

}