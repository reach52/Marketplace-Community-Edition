package reach52.marketplace.community.fragments.insurance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Option
import arrow.core.getOrElse
import com.google.gson.Gson
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance_cambodia.BenefitAdapter
import reach52.marketplace.community.extensions.toCambodiaCurrency
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.insurance.view.MembersFragment
import reach52.marketplace.community.models.insurance.InsurancePurchase
import reach52.marketplace.community.models.policy_owner.Beneficiaries
import reach52.marketplace.community.models.policy_owner.Insured
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_plan_detail.*
import kotlinx.android.synthetic.main.fragment_insurance_plan_detail.view.*
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@Suppress("UNCHECKED_CAST")
class InsurancePlanDetailFragment : Fragment() {

	private val REQUEST_QUESTIONNAIRE = 1
	private val REQUEST_BENEFICIARY = 0

	private val disposable = CompositeDisposable()
	private val benefitAdapter = BenefitAdapter()
	private var hasQuestions = false
	private lateinit var insurerName: String
	private lateinit var residentName: String
	private lateinit var residentID: String
	private lateinit var residentBirthDate: ZonedDateTime
	private lateinit var residentGender: String
	private lateinit var residentAddress: String
	private lateinit var residentContact: String
	private lateinit var residentEmail: String
	private lateinit var residentCivilStatus: String
	private var insuredBeneficiaryIntent = Intent()
	private var questionnaireIntent = Intent()


	private lateinit var mInsuranceViewModel: Option<InsuranceViewModel>

	private val insurancePurchase by lazy {
		InsurancePurchase(DispergoDatabase.getInstance(context!!))
	}

//	private val user by lazy {
//		Auth.currentSession(context!!).map {
//			it.user
//		}
//	}

	private val residentLogisticViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	private val insuranceViewModel by lazy {
		Option.fromNullable(activity).map {
			ViewModelProvider(it)[InsuranceViewModel::class.java]
		}
	}

	private val insuranceVMSetter by lazy {
		activity?.run {
			ViewModelProvider(this)[InsuranceViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
		mInsuranceViewModel = Option.fromNullable(activity).map {
			ViewModelProvider(it)[InsuranceViewModel::class.java]
		}
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.app_bar_search).isVisible = false
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		insuranceVMSetter.resetQuestionnaire()
		insuranceViewModel.map { model ->
			model.selectedInsurerFormularyRate.observe(viewLifecycleOwner, Observer {
				if (Locale.getDefault().displayLanguage == "English") {
					priceTextView.text = it.toPhilippinesCurrency()
				} else {
					priceTextView.text = it.toCambodiaCurrency()
				}
			})

			model.selectedInsurerFormularyTitle.observe(viewLifecycleOwner, Observer {
				benefitTitleTextView.text = it
			})

			model.selectedInsurerFormularyBenefits.observe(viewLifecycleOwner, Observer {
				benefitAdapter.submitList(it)
			})

			if (residentLogisticViewModel.isLogisticResident.value == true) {
				residentName = model.selectedLogisticResident.value?.name.toString()
				residentID = model.selectedLogisticResident.value?.residentId.toString()
				residentBirthDate = model.selectedLogisticResident.value?.dateOfBirth!!
				residentGender = model.selectedLogisticResident.value?.gender.toString()
				residentAddress = model.selectedLogisticResident.value?.address.toString()
				residentContact = model.selectedLogisticResident.value?.contact.toString()
				residentEmail = model.selectedLogisticResident.value?.email.toString()
				residentCivilStatus = model.selectedLogisticResident.value?.maritalStatus.toString()
			} else {
				residentName = model.selectedResident.value?.name.toString()
				residentID = model.selectedResident.value?.id.toString()
				residentBirthDate = model.selectedResident.value?.birthDate!!.atStartOfDay(ZoneId.systemDefault())
				residentGender = model.selectedResident.value?.gender.toString()
				residentAddress = model.selectedResident.value?.address!!.text
				residentContact = model.selectedResident.value?.contact!!.getOrElse { "" }
				residentEmail = model.selectedResident.value?.email!!.getOrElse { "" }
				residentCivilStatus = model.selectedResident.value?.maritalStatus!!.getOrElse { "" }
			}

			if (!insuranceVMSetter.selectedInsurer.value?.qualification?.questionnaire?.url.equals("")) {
				hasQuestions = true
				insurerName = model.selectedInsurer.value?.display.toString()
				questionnaireIntent = Intent("LOAD_KOBO_QUESTIONNAIRE").apply {
					type = "application/json"
					putExtra("data", hashMapOf<String, Any>(
							"koboUrl" to model.selectedInsurer.value?.qualification?.questionnaire?.url.toString(),
							"koboUsername" to "r52_kobo_mandl",
							"koboPassword" to "HealthAll2020_ml",
							"title" to "Questions"
					))
				}
			}

		}

		insuredBeneficiaryIntent = Intent("LOAD_KOBO_QUESTIONNAIRE").apply {
			type = "application/json"
			putExtra("data", hashMapOf<String, Any>(
					"koboUrl" to "",
					"koboUsername" to "",
					"koboPassword" to "",
					"title" to "Insured and beneficiary information"
			))
		}

		return inflater.inflate(R.layout.fragment_insurance_plan_detail, container, false).apply {
			(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.insurance_plan_details)

			benefitsRecyclerView.apply {
				layoutManager = LinearLayoutManager(activity)
				adapter = benefitAdapter
				addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(activity).orientation))
			}

			disposable.add(benefitProceedButton.clicks().subscribe {

				if (hasQuestions) {
					if (questionnaireIntent.resolveActivity(context.packageManager) != null) {
						startActivityForResult(questionnaireIntent, REQUEST_QUESTIONNAIRE)
					} else {
						Toast.makeText(context, context.getString(R.string.install_access_continue), Toast.LENGTH_SHORT).show()
					}
				} else {
					if (insuredBeneficiaryIntent.resolveActivity(context.packageManager) != null) {
						startActivityForResult(insuredBeneficiaryIntent, REQUEST_BENEFICIARY)
					} else {
						Toast.makeText(context, context.getString(R.string.install_access_continue), Toast.LENGTH_SHORT).show()
					}
				}


			})

			disposable.add(benefitCancelButton.clicks().subscribe {
				parentFragmentManager.popBackStack()
			})
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (resultCode) {
			Activity.RESULT_OK -> {
				val strData = data?.extras?.getString("data").toString()
				val mapData = (Gson().fromJson(strData, Map::class.java) as Map<String, String>)
				when (requestCode) {
					REQUEST_BENEFICIARY -> {
						val answersData: Map<String, String> = mapData["answers"] as Map<String, String>
						if (answersData["insuredTo"].toString() == "Self") {
							insuranceVMSetter.policyOwnerInsured.value = Insured(
									reference = residentID,
									display = residentName,
									address = answersData["currentAddress"].toString(),
									email = answersData["email"].toString(),
									contact = answersData["contactNumber"].toString(),
									civilStatus = answersData["civilStatus"].toString(),
									gender = residentGender,
									dateOfBirth = residentBirthDate,
									relationship = ""
							)
						} else if (answersData["insuredTo"].equals("Someone")) {
							insuranceVMSetter.policyOwnerInsured.value = Insured(
									reference = "",
									display = answersData["fullNameForSomeone"].toString(),
									address = answersData["currentAddressForSomeone"].toString(),
									email = answersData["emailForSomeone"].toString(),
									contact = answersData["contactNumberForSomeone"].toString(),
									civilStatus = answersData["civilStatusForSomeone"].toString(),
									gender = answersData["gender"].toString(),
									dateOfBirth = ZonedDateTime.parse(answersData["dateOfBirth"].toString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME),
									relationship = answersData["insuredRelationship"].toString()
							)
						}

						insuranceVMSetter.resetBeneficiaries()
						insuranceVMSetter.addedBeneficiaries.value?.add(Beneficiaries(
								display = answersData["beneficiaryFullname"].toString(),
								relationship = answersData["beneficiaryRelationship"].toString(),
								reference = ""
						))

						insuranceVMSetter.policyOwnerID.value = residentID
						insuranceVMSetter.policyOwnerFullName.value = residentName
						insuranceVMSetter.policyOwnerAddress.value = answersData["currentAddress"].toString()
						insuranceVMSetter.policyOwnerEmail.value = answersData["email"].toString()
						insuranceVMSetter.policyOwnerContact.value = answersData["contactNumber"].toString()
						insuranceVMSetter.policyOwnerCivilStatus.value = answersData["civilStatus"].toString()
						insuranceVMSetter.policyOwnerGender.value = residentGender
						insuranceVMSetter.policyOwnerDateOfBirth.value = residentBirthDate

						insuranceVMSetter.selectedInsurerPlan.value?.category.apply {
							if (this!!.contains("family plan")) {
								parentFragmentManager.beginTransaction().apply {
									replace(R.id.fragment_container, MembersFragment())
									addToBackStack(null)
									commit()
								}
							} else {
								parentFragmentManager.beginTransaction().apply {
									replace(R.id.fragment_container, InsurancePurchasePreviewFragment())
									addToBackStack(null)
									commit()
								}

							}
						}


					}
					REQUEST_QUESTIONNAIRE -> {
						insuranceVMSetter.policyOwnerQualification.value?.data = mapData

						val answersData: Map<String, String> = mapData["answers"] as Map<String, String>
						var hasYesAnswers = false
						loop@ for (it in answersData.values) {
							when (it) {
								"Yes" -> {
									hasYesAnswers = true
									break@loop
								}
							}
						}
						if (hasYesAnswers) {
							//save and inform to function
							val insurerID = insuranceVMSetter.selectedInsurerId.value.toString()
							val unit = insuranceVMSetter.unit.value.toString()
							insuranceVMSetter.selectedInsurerPlan.value?.let {
//								insurancePurchase.addPolicyInfo(
//										insuranceId = insurerID,
//										insurerName = insurerName,
//										policyOwnerID = residentID,
//										policyOwnerFullName = residentName,
//										policyOwnerAddress = residentAddress,
//										policyOwnerEmail = residentEmail,
//										policyOwnerContact = residentContact,
//										policyOwnerCivilStatus = residentCivilStatus,
//										policyOwnerGender = residentGender,
//										policyOwnerDateOfBirth = residentBirthDate,
//										formulary = it,
//										user = user,
//										unit = unit,
//										answers = mapData
//								)
							}
							Toast.makeText(context, context!!.getString(R.string.working_days), Toast.LENGTH_LONG).show()
							parentFragmentManager.popBackStack("residentDetails", 0)

						} else {
							insuranceVMSetter.mapDataQuestionnaire.value = mapData
							startActivityForResult(insuredBeneficiaryIntent, REQUEST_BENEFICIARY)
						}

					}
				}
			}
		}

	}

}