package reach52.marketplace.community.fragments.insurance

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Option
import com.google.gson.Gson
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance.InsuredAdapter
import reach52.marketplace.community.insurance.view.InsurancePurchaseActivity
import reach52.marketplace.community.models.insurance.InsurancePurchase
import reach52.marketplace.community.models.policy_owner.Beneficiaries
import reach52.marketplace.community.models.policy_owner.Insured
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.policyOwner_mapper.PolicyOwnerMapper
import reach52.marketplace.community.resident.view.ResidentDetailsActivity
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import reach52.marketplace.community.viewmodels.OrderViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_policy_owner.view.*
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
@ExperimentalUnsignedTypes
class PolicyOwnerFragment : Fragment() {

	private val disposable = CompositeDisposable()
	private lateinit var mModel: Option<OrderViewModel>
	private lateinit var residentName: String
	private lateinit var residentID: String
	private lateinit var residentBirthDate: ZonedDateTime
	private var residentGender = ""

	private lateinit var vm: ResidentViewModel

	private val insuranceViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[InsuranceViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	private val insuranceViewModelGetter by lazy {
		Option.fromNullable(activity).map {
			ViewModelProvider(it)[InsuranceViewModel::class.java]
		}
	}
//    private val residentLogisticViewModel by lazy {
//        activity?.run {
//            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
//        } ?: throw Exception("Invalid Activity")
//    }

	private val insurancePurchase by lazy {
		InsurancePurchase(DispergoDatabase.getInstance(context!!))
	}

	private val insuredAdapter = InsuredAdapter {
		val policyOwner = insurancePurchase.get(it)
		insuranceViewModel.policyOwnerDocumentId.value = it
		when (policyOwner.status) {
			"active", "pending issuance", "cancelled" -> {
				val fragment = InsurancePurchasePreviewFragment()
				parentFragmentManager.beginTransaction().apply {
					replace(R.id.fragment_container, fragment)
					addToBackStack(null)
					commit()
				}
			}
			"declined" -> {
				val fragment = DeclinedPlanDetailsFragment()
				parentFragmentManager.beginTransaction().apply {
					replace(R.id.fragment_container, fragment)
					addToBackStack(null)
					commit()
				}

			}
			"approved" -> {
				val insuredBeneficiaryIntent = Intent("LOAD_KOBO_QUESTIONNAIRE").apply {
					type = "application/json"
					putExtra("data", hashMapOf<String, Any>(
							"koboUrl" to "",
							"koboUsername" to "",
							"koboPassword" to "",
							"title" to "Insured and beneficiary information"
					))
				}
				startActivityForResult(insuredBeneficiaryIntent, 0)
			}
		}
	}

	private val policyInsuredQuery by lazy {
		DispergoDatabase.purchasedInsurancePlansView(context!!).createQuery()
	}

	private val insuredLiveQuery by lazy {
		policyInsuredQuery.toLiveQuery()
	}

	private val insuredFactory by lazy {
		EntityDataSourceFactory(policyInsuredQuery::run, PolicyOwnerMapper())
	}

	private val insured by lazy {
		LivePagedListBuilder(insuredFactory, EntityDataSource.PAGED_LIST_CONFIG).build()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

		vm = ViewModelProvider(activity as ResidentDetailsActivity)[ResidentViewModel::class.java]
		insuranceViewModel.selectedLogisticResident.value = vm.resident.toLogisticResident()
		mModel = Option.fromNullable(activity).map {
			ViewModelProvider(it)[OrderViewModel::class.java]
		}

		if (vm.isLogisticResident) {
			policyInsuredQuery.apply {
				startKey = insuranceViewModel.selectedLogisticResident.value?.residentId
				endKey = insuranceViewModel.selectedLogisticResident.value?.residentId
			}
		} else {
			policyInsuredQuery.apply {
				startKey = insuranceViewModel.selectedResident.value?.id
				endKey = insuranceViewModel.selectedResident.value?.id
			}
		}

		insuredLiveQuery.apply {
			addChangeListener { event ->
				if (event.source == this) {
					insuredFactory.fnEnumerator = { event.rows }
					insured.value?.dataSource?.invalidate()
				}
			}
			start()
		}

	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		insured.observe(viewLifecycleOwner, Observer { it ->
			insuredAdapter.submitList(it?.sortedByDescending {
				it?.record?.lastUpdated
			})
		})

		insuranceViewModelGetter.map { model ->
			if (vm.isLogisticResident) {
				residentName = model.selectedLogisticResident.value?.name.toString()
				residentID = model.selectedLogisticResident.value?.residentId.toString()
				residentBirthDate = model.selectedLogisticResident.value?.dateOfBirth!!
				residentGender = model.selectedLogisticResident.value?.gender.toString()
			} else {
				residentName = model.selectedResident.value?.name.toString()
				residentID = model.selectedResident.value?.id.toString()
				residentBirthDate = model.selectedResident.value?.birthDate!!.atStartOfDay(ZoneId.systemDefault())
				residentGender = model.selectedResident.value?.gender.toString()
			}

		}

		return inflater.inflate(R.layout.fragment_policy_owner, container, false).apply {
			policyOwnerRecyclerView.apply {
				layoutManager = LinearLayoutManager(activity)
				adapter = insuredAdapter
			}

			if (vm.isLogisticResident == true) {
				age_not_qualified.visibility = View.INVISIBLE

				mModel.flatMap { it.selectedLogisticResident }.map {
					when {
						(it.age < 18 || it.age > 65) -> {
							floatingActionButton.hide()
							age_not_qualified.visibility = View.VISIBLE
						}
					}
				}
			} else {
				age_not_qualified.visibility = View.INVISIBLE
				mModel.flatMap { it.selectedResident }.map {
					when {
						(it.age < 18 || it.age > 65) -> {
							floatingActionButton.hide()
							age_not_qualified.visibility = View.VISIBLE

						}
					}
				}
			}

			disposable.add(floatingActionButton.clicks().subscribe {
//                insuranceViewModel.policyOwnerDocumentId.value = null
//                parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, InsuranceConsentFragment())
//                        .addToBackStack(null)
//                        .commit()

				startActivity(Intent(activity, InsurancePurchaseActivity::class.java).apply {
                    this.putExtra(InsurancePurchaseActivity.KEY_RESIDENT_ID, vm.resident.id)
				})

			})
		}
	}

	@SuppressLint("ShowToast")
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (resultCode) {
			Activity.RESULT_OK -> {
				val strData = data?.extras?.getString("data").toString()
				val mapData = (Gson().fromJson(strData, Map::class.java) as Map<String, String>)
				when (requestCode) {
					0 -> {
						val answersData: Map<String, String> = mapData["answers"] as Map<String, String>
						if (answersData["insuredTo"].toString() == "Self") {


							insuranceViewModel.policyOwnerInsured.value = Insured(
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
							insuranceViewModel.policyOwnerInsured.value = Insured(
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

						insuranceViewModel.addedBeneficiaries.value?.add(Beneficiaries(
								display = answersData["beneficiaryFullname"].toString(),
								relationship = answersData["beneficiaryRelationship"].toString(),
								reference = ""
						))

						insuranceViewModel.policyOwnerID.value = residentID
						insuranceViewModel.policyOwnerFullName.value = residentName
						insuranceViewModel.policyOwnerAddress.value = answersData["currentAddress"].toString()
						insuranceViewModel.policyOwnerEmail.value = answersData["email"].toString()
						insuranceViewModel.policyOwnerContact.value = answersData["contactNumber"].toString()
						insuranceViewModel.policyOwnerCivilStatus.value = answersData["civilStatus"].toString()
						insuranceViewModel.policyOwnerGender.value = residentGender
						insuranceViewModel.policyOwnerDateOfBirth.value = residentBirthDate

						parentFragmentManager.beginTransaction().apply {
							replace(R.id.fragment_container, InsurancePurchasePreviewFragment())
							addToBackStack(null)
							commit()
						}
					}
				}
			}
		}

	}
}
