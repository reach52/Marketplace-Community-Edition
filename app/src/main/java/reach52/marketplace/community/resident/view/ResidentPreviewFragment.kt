package reach52.marketplace.community.resident.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_resident_preview.*
import kotlinx.android.synthetic.main.fragment_resident_preview.view.*
import org.threeten.bp.format.DateTimeFormatter

class ResidentPreviewFragment : Fragment() {

	private val disposables = CompositeDisposable()

	private lateinit var vm: ResidentViewModel

	private val residentLogisticViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	@ExperimentalUnsignedTypes
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		vm = ViewModelProvider(activity as BaseActivity)[ResidentViewModel::class.java]

		return inflater.inflate(R.layout.fragment_resident_preview, container, false).apply {
			(activity as BaseActivity).supportActionBar?.title = context.getString(R.string.resident_previwe_details)

			disposables.add(saveButtonResidentPreview.clicks().subscribe {
				val ctx = context

				if (vm.update) {
                    vm.updateResident(ctx).subscribe{
                        Toast.makeText(context, context.getString(R.string.Updated), Toast.LENGTH_SHORT).show()
	                    (activity as BaseActivity).setResult(ResidentDetailsActivity.RESULT_UPDATE)
                        (activity as BaseActivity).finish()
                    }
				} else {
					vm.saveNewResident(ctx).subscribe {
						Toast.makeText(context, getString(R.string.done), Toast.LENGTH_SHORT).show()
						(activity as BaseActivity).finish()
					}
				}

//                arrow.instances.option.monad.binding {
//                    val resident = Option.fromNullable(residentLogisticViewModel.selectedLogisticResident.value).bind()
//                    val residentId: String
//                    residentId = if (residentLogisticViewModel.isLogisticResidentUpdate.value == true) {
//                        client.updateResident(
//                                resident = resident,
//                                user = user
//                        )
//                        residentLogisticViewModel.selectedLogisticResident.value!!.residentId
//                    } else {
//                        client.new(
//                                resident = resident,
//                                user = user
//                        )
//                    }
//
//                    val residentDetails = client.get(residentId)
//                    AlertDialog.Builder(ctx).setMessage("Do you want to save the changes/new resident?")
//                            .setPositiveButton("Yes") { _, _ ->
//                                insuranceViewModel.selectedLogisticResident.value = residentDetails
//                                orderViewModel.map { model -> model.selectedLogisticResident = Some(residentDetails) }
//                                residentLogisticViewModel.isLogisticResident.value = true
//
//                                parentFragmentManager
//                                        .beginTransaction()
//                                        .replace(R.id.fragment_container, ResidentDetailsFragment())
//                                        .commit()
//                            }
//                            .setNegativeButton("No") { _, _ ->
//                                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                            }.create().apply {
//                                setOnCancelListener {
//                                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                                }
//                            }.show()
//                }
			})

			disposables.add(cancelButtonResidentPreview.clicks().subscribe {
				parentFragmentManager.popBackStack()
			})
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		showResidentData(vm.resident)

	}

	override fun onDestroy() {
		disposables.clear()
		super.onDestroy()
	}

	private fun showResidentData(resident: Resident) {

		firstNameTextDisplay.text = resident.firstName
		middleNameTextDisplay.text = resident.middleName
		lastNameTextDisplay.text = resident.lastName
		contactTextDisplay.text = resident.phone
		emailTextDisplay.text = resident.email
		dateOfBirthTextDisplay.text = resident.dob.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
		provinceTextDisplay.text = resident.city
		addressTextDisplay.text = resident.addressLine
		postalTextDisplay.text = resident.zipCode
		genderTextDisplay.text = resident.gender
		maritalStatusTextDisplay.text = resident.maritalStatus
		countryTextDisplay.text = resident.country

	}

}
