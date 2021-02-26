package reach52.marketplace.community.insurance.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.LayoutNewMembersBinding
import reach52.marketplace.community.extensions.utils.createDatePicker
import reach52.marketplace.community.extensions.utils.dateTimeStringToZonedDateTime
import reach52.marketplace.community.insurance.adapter.MemberListAdapter
import reach52.marketplace.community.insurance.entity.InsuranceProduct
import reach52.marketplace.community.insurance.entity.Member
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.fragment_members.view.*
import java.text.SimpleDateFormat
import java.util.*

class MembersFragment : Fragment() {

	private lateinit var ctx: Context
	private lateinit var vm: InsurancePurchaseViewModel
	private lateinit var construct: InsuranceProduct.Construct

	private lateinit var adapter: MemberListAdapter

	private lateinit var newMemberDialog: AlertDialog
	lateinit var vmr: ResidentViewModel

	//	private lateinit var dialogLayout: View
	private lateinit var b: LayoutNewMembersBinding
	private var dob: String = ""

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
			it.isVisible = true

			it.setOnMenuItemClickListener {
				vm.saveMembers().subscribe(
						{
							(activity as InsurancePurchaseActivity).gotoPremiumFragment()
						},
						{
							Toast.makeText(ctx, it.message, Toast.LENGTH_SHORT).show()
						}
				)
				true
			}
		}

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





	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		val activity = (activity as InsurancePurchaseActivity)
		vm = ViewModelProvider(activity)[InsurancePurchaseViewModel::class.java]
		vmr = ViewModelProvider(activity)[ResidentViewModel::class.java]
		construct = vm.selectedConstruct
		if (vm.selectedMembers.isNotEmpty()) {
			vm.selectedMembers.clear()
		}
		val resident = vm.resident
		vm.selectedMembers.add(Member(
				resident.firstName,
				resident.lastName,
				resident.dob,
				resident.gender,
				vm.getPrimaryParty(vm)
		))
		adapter = MemberListAdapter(ctx, vm.selectedMembers)



		return inflater.inflate(R.layout.fragment_members, container, false).apply {
			(activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.dependents)

			createdNewMemberDialog()
			members_list.adapter = adapter

			if (construct.extraParties.isEmpty()) {
				add_member_btn.visibility = View.GONE
			}
			add_member_btn.setOnClickListener {

				clearDialog()
				newMemberDialog.show()

			}

		}

	}

	//click on home button 16-12-2020
	/*override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			R.id.home_action -> {

				try {
					(activity as AppCompatActivity).startActivity(Intent((activity as AppCompatActivity), ResidentDetailsActivity::class.java).apply {
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

	private fun createdNewMemberDialog() {

		setupNewMemberDialogLayout()

		val addClickListener = View.OnClickListener {

			if (dob.isEmpty()) {
				Toast.makeText(ctx, getString(R.string.date_cannot_be_empty), Toast.LENGTH_SHORT).show()
				return@OnClickListener
			}

			val firstName = b.memberFirstName.text.toString()
			val lastName = b.memberLastName.text.toString()
			val dob = dateTimeStringToZonedDateTime(dob + "T00:00:00-05:00")
			val gender = b.memberGender.selectedItem.toString()
			val party = b.memberRelation.selectedItem as InsuranceProduct.ConstructParty

			val member = Member(
					firstName,
					lastName,
					dob,
					gender,
					party
			)

			try {
				vm.validateMember(member)
				adapter.addMember(member)
				newMemberDialog.dismiss()
			} catch (e: Exception) {
				when (e) {
					is InsurancePurchaseViewModel.EmptyNameException -> {
						Toast.makeText(ctx, getString(R.string.name_cannot_be_empty), Toast.LENGTH_SHORT).show()
					}
					is InsurancePurchaseViewModel.AgeTooLowException -> {
						Toast.makeText(ctx, getString(R.string.min_age_required) + e.message, Toast.LENGTH_SHORT).show()
					}
					is InsurancePurchaseViewModel.AgeTooHighException -> {
						Toast.makeText(ctx, getString(R.string.max_age_is) + e.message, Toast.LENGTH_SHORT).show()
					}
					else -> {
						Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
					}
				}
			}

		}

		newMemberDialog = AlertDialog.Builder(ctx)
				.setView(b.root)
				.setCancelable(false)
				.setNegativeButton(getString(R.string.cancel), null)
				.setPositiveButton(getString(R.string.add), null)
				.create()
		newMemberDialog.setOnShowListener {
			newMemberDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(addClickListener)
		}

	}



	private fun setupNewMemberDialogLayout() {
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

		val relationAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, construct.extraParties)
		b.memberRelation.adapter = relationAdapter
		b.contactGroup.visibility = View.GONE

	}

	private fun clearDialog() {
		clearEditText(b.memberFirstName)
		clearEditText(b.memberLastName)
		clearEditText(b.memberDob)
		dob = ""

	}

	private fun clearEditText(et: EditText) {
		et.text.clear()
		et.clearFocus()
	}

	override fun onResume() {
		super.onResume()
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.members)
	}



}