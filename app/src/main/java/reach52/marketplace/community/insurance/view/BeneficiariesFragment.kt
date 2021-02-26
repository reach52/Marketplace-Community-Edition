package reach52.marketplace.community.insurance.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.LayoutNewMembersBinding
import reach52.marketplace.community.extensions.utils.createDatePicker
import reach52.marketplace.community.extensions.utils.dateTimeStringToZonedDateTime
import reach52.marketplace.community.insurance.adapter.BeneficiaryListAdapter
import reach52.marketplace.community.insurance.entity.Beneficiary
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.fragment_beneficiaries.*
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import java.util.*

class BeneficiariesFragment : Fragment() {

	private lateinit var ctx: Context
	private lateinit var vm: InsurancePurchaseViewModel

	private lateinit var adapter: BeneficiaryListAdapter
	private lateinit var newBeneficiaryDialog: AlertDialog
	private lateinit var saveMenu: MenuItem
	private lateinit var b: LayoutNewMembersBinding
	private var dob: String = ""
	lateinit var vmr: ResidentViewModel
	override fun onAttach(context: Context) {
		super.onAttach(context)
		this.ctx = context
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.save).let {
			it.isVisible = vm.beneficiaries.isNotEmpty()
			it.setOnMenuItemClickListener {
				vm.saveBeneficiaries().subscribe(
						{
							(activity as InsurancePurchaseActivity).gotoIDUploadFragment()
						},
						{ err ->
							Toast.makeText(ctx, err.message, Toast.LENGTH_SHORT).show()
						}
				)
				true
			}
			saveMenu = it
		}

		menu.findItem(R.id.home_action).let {
			it.isVisible = true

			it.setOnMenuItemClickListener {
				try {

					(activity as InsurancePurchaseActivity).gotoOrderPreviewhome(context!!)
				} catch (e: java.lang.Exception) {
					System.out.println("onclick home= " + e.message)
				}
				true
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val activity = activity as InsurancePurchaseActivity

		vm = ViewModelProvider(activity)[InsurancePurchaseViewModel::class.java]
		vmr = ViewModelProvider(activity)[ResidentViewModel::class.java]
		return inflater.inflate(R.layout.fragment_beneficiaries, container, false).apply {

			adapter = BeneficiaryListAdapter(ctx, vm.beneficiaries) {
				updateUIForListSize(it)
			}


			createdNewBeneficiaryDialog()


		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		beneficiary_list.adapter = adapter
		add_beneficiary_btn.setOnClickListener {

			clearDialog()
			newBeneficiaryDialog.show()

		}

		// click on add benificiary text
		add_beneficiary_message.setOnClickListener {

			clearDialog()
			newBeneficiaryDialog.show()

		}
	}

	private fun updateUIForListSize(size: Int) {
		if (size == 0) {
			//add_beneficiary_message.visibility = View.VISIBLE
			addbeneficiry_layout.visibility = View.VISIBLE
			beneficiary_list.visibility = View.GONE
			if (::saveMenu.isInitialized) {
				saveMenu.isVisible = false
			}
		} else {
			//add_beneficiary_message.visibility = View.GONE
			addbeneficiry_layout.visibility = View.GONE
			beneficiary_list.visibility = View.VISIBLE
			if (::saveMenu.isInitialized) {
				saveMenu.isVisible = false
			}
			saveMenu.isVisible = true
		}

		if (size >= vm.selectedInsuranceProduct.maxBeneficiaries) {
			add_beneficiary_btn.visibility = View.GONE
		} else {
			add_beneficiary_btn.visibility = View.VISIBLE
		}
	}

	private fun createdNewBeneficiaryDialog() {

		setupNewBeneficiaryDialogLayout()

		val addClickListener = View.OnClickListener {

			if (dob.isEmpty()) {
				Toast.makeText(ctx, getString(R.string.date_cannot_be_empty), Toast.LENGTH_SHORT).show()
				return@OnClickListener
			}

			val firstName = b.memberFirstName.text.toString()
			val lastName = b.memberLastName.text.toString()
			val dob = dateTimeStringToZonedDateTime(dob + "T00:00:00-05:00")
			val gender = b.memberGender.selectedItem.toString()
			val relation = b.memberRelation.selectedItem.toString()
			val phone = b.memberPhone.text.toString()
			val address1 = b.memberAddressLine1.text.toString()
			val address2 = b.memberAddressLine2.text.toString()
			val city = b.memberCity.text.toString()
			val country = b.memberCountry.text.toString()
			val zipCode = b.memberZipCode.text.toString()

			val beneficiary = if (BuildConfig.BUILD_TYPE == "debug") {
				getDummyBeneficiary()
			} else {
				Beneficiary(
						firstName,
						lastName,
						dob,
						gender,
						relation,
						phone,
						address1,
						address2,
						city,
						country,
						zipCode
				)

			}

			try {
				vm.validateBeneficiary(beneficiary)
				adapter.addBeneficiary(beneficiary)
				newBeneficiaryDialog.dismiss()
			} catch (e: Exception) {

				when (e) {
					is InsurancePurchaseViewModel.EmptyNameException -> {
						Toast.makeText(ctx, getString(R.string.name_cannot_be_empty), Toast.LENGTH_SHORT).show()
					}
					is InsurancePurchaseViewModel.InvalidPhoneException -> {
						Toast.makeText(ctx, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show()
					}
					is InsurancePurchaseViewModel.IncompleteAddressException -> {
						Toast.makeText(ctx, getString(R.string.address_is_incomplete), Toast.LENGTH_SHORT).show()
					}
					else -> {
						Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
					}
				}
			}

		}

		newBeneficiaryDialog = AlertDialog.Builder(ctx)
				.setView(b.root)
				.setCancelable(false)
				.setNegativeButton(getString(R.string.cancel), null)
				.setPositiveButton(getString(R.string.add), null)
				.create()
		newBeneficiaryDialog.setOnShowListener {
			newBeneficiaryDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(addClickListener)
		}

	}

	private fun setupNewBeneficiaryDialogLayout() {
		val dateFormat = SimpleDateFormat("yyyy-MM-dd")

		b = LayoutNewMembersBinding.inflate(LayoutInflater.from(ctx))

		b.memberDob.setOnClickListener { view ->

			val datePicker = createDatePicker(ctx) { _, year, month, dayOfMonth ->
				run {
					val cal = Calendar.getInstance()
					cal.set(year, month, dayOfMonth)
					dob = dateFormat.format(cal.time)
					(view as EditText).setText("$dayOfMonth/${month.plus(1)}/$year")
				}
			}
			datePicker.datePicker.maxDate = System.currentTimeMillis()
			datePicker.show()
		}
		b.contactGroup.visibility = View.VISIBLE

	}

	private fun clearDialog() {
		clearEditText(b.memberFirstName)
		clearEditText(b.memberLastName)
		clearEditText(b.memberDob)
		clearEditText(b.memberPhone)
		clearEditText(b.memberAddressLine1)
		clearEditText(b.memberAddressLine2)
		clearEditText(b.memberCity)
		clearEditText(b.memberCountry)
		clearEditText(b.memberZipCode)
		dob = ""
	}

	private fun clearEditText(et: EditText) {
		et.text.clear()
		et.clearFocus()
	}


	//click on home button 16-12-2020
	/*override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
		updateUIForListSize(vm.beneficiaries.size)
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.beneficiaries)
	}

	fun getDummyBeneficiary() = Beneficiary(
			"Brad",
			"Pitt",
			ZonedDateTime.now().minusYears(40),
			"male",
			"spouse",
			"9930620323",
			"block 111, city",
			"2nd Streeet",
			"Mumbai",
			"india",
			"123233"
	)

}
