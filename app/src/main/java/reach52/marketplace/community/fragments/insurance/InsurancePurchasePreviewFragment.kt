package reach52.marketplace.community.fragments.insurance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance_cambodia.BenefitAdapter
import reach52.marketplace.community.extensions.toCambodiaCurrency
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.insurance.InsurancePurchase
import reach52.marketplace.community.models.insurance.Insurers
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_purchase_preview.view.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class InsurancePurchasePreviewFragment : Fragment() {

	private val benefitAdapter = BenefitAdapter()
	private val disposables = CompositeDisposable()

	private val insuranceViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[InsuranceViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	private val insurancePurchase by lazy {
		InsurancePurchase(DispergoDatabase.getInstance(context!!))
	}

	private val insurerDocument by lazy {
		Insurers(DispergoDatabase.getInstance(context!!))
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_insurance_purchase_preview, container, false).apply {
			(activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.purchase_preview)

			when (insuranceViewModel.policyOwnerDocumentId.value == null) {
				true -> {
					insuranceViewModel.insuranceName.observe(viewLifecycleOwner, Observer { insuranceNameDisplay.text = it })
					insuranceViewModel.selectedInsurerFormularyRate.observe(viewLifecycleOwner, Observer {
						if (Locale.getDefault().displayLanguage == "English") {
							priceTextView.text = it.toPhilippinesCurrency()
						} else {
							priceTextView.text = it.toCambodiaCurrency()
						}
					})
					insuranceViewModel.selectedInsurerFormularyTitle.observe(viewLifecycleOwner, Observer { planNameDisplay.text = it })
					insuranceViewModel.selectedInsurerFormularyBenefits.observe(viewLifecycleOwner, Observer { benefitAdapter.submitList(it) })
					insuranceViewModel.selectedPolicyInsured.observe(viewLifecycleOwner, Observer { insuredPersonName.text = it.display })
					insuranceViewModel.selectedPolicyOwnerFullName.observe(viewLifecycleOwner, Observer { policyOwnerName.text = it })
					insuranceViewModel.selectedBeneficiaries.observe(viewLifecycleOwner, Observer {
						beneficiaryNameTextView.text = it.first().display
						relationshipTextView.text = it.first().relationship
					})
				}
				false -> {
					val policyOwner = insurancePurchase.get(insuranceViewModel.policyOwnerDocumentId.value?.toString()!!)

					insuranceViewModel.dependents.clear()
					insuranceViewModel.dependents.addAll(policyOwner.dependents as List)

					when (policyOwner.status) {
						"approved" -> {
							val insurerDetail = insurerDocument.get(policyOwner.insurer.reference)
							insuranceViewModel.selectedInsurer.value = insurerDetail
							insuranceViewModel.selectedInsurerPlan.value = insurerDetail.formulary.first()
							insuranceViewModel.insuranceName.observe(viewLifecycleOwner, Observer { insuranceNameDisplay.text = it })
							insuranceViewModel.selectedInsurerFormularyRate.observe(viewLifecycleOwner, Observer {
								if (Locale.getDefault().displayLanguage == "English") {
									priceTextView.text = it.toPhilippinesCurrency()
								} else {
									priceTextView.text = it.toCambodiaCurrency()
								}
							})
							insuranceViewModel.selectedInsurerFormularyTitle.observe(viewLifecycleOwner, Observer { planNameDisplay.text = it })
							insuranceViewModel.selectedInsurerFormularyBenefits.observe(viewLifecycleOwner, Observer { benefitAdapter.submitList(it) })
							insuranceViewModel.selectedPolicyInsured.observe(viewLifecycleOwner, Observer { insuredPersonName.text = it.display })
							insuranceViewModel.selectedPolicyOwnerFullName.observe(viewLifecycleOwner, Observer { policyOwnerName.text = it })
							insuranceViewModel.selectedBeneficiaries.observe(viewLifecycleOwner, Observer {
								beneficiaryNameTextView.text = it.first().display
								relationshipTextView.text = it.first().relationship
							})

						}
						"active", "pending issuance", "cancelled" -> {
							insuranceNameDisplay.text = policyOwner.insurer.insurerName
							if (Locale.getDefault().displayLanguage == "English") {
								priceTextView.text = policyOwner.insurer.plan.first().rate.toPhilippinesCurrency()
							} else {
								priceTextView.text = policyOwner.insurer.plan.first().rate.toCambodiaCurrency()
							}
							planNameDisplay.text = policyOwner.insurer.plan.first().tier
							benefitAdapter.submitList(policyOwner.insurer.plan.first().benefits)
							insuredPersonName.text = policyOwner.insured.display
							policyOwnerName.text = policyOwner.policyOwnerFullName
							beneficiaryNameTextView.text = policyOwner.beneficiaries.first().display
							relationshipTextView.text = policyOwner.beneficiaries.first().relationship
							previewProceedButton.visibility = View.GONE
							previewCancelButton.text = getString(R.string.go_back)
						}
					}
				}
			}

			disposables.add(policyOwnerCardView.clicks().subscribe {
				(activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.policy_owner_details)
				val fragment = PolicyOwnerDetailsFragment()
				fragment.arguments = bundleOf("data" to "policyOwner")
				parentFragmentManager.beginTransaction().apply {
					replace(R.id.fragment_container, fragment)
					addToBackStack(null)
					commit()
				}
			})

			disposables.add(insuredCardView.clicks().subscribe {
				(activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.insured_details)
				val fragment = PolicyOwnerDetailsFragment()
				fragment.arguments = bundleOf("data" to "insured")
				parentFragmentManager.beginTransaction().apply {
					replace(R.id.fragment_container, fragment)
					addToBackStack(null)
					commit()
				}
			})


			disposables.add(previewProceedButton.clicks().subscribe {
				parentFragmentManager.beginTransaction().apply {
					replace(R.id.fragment_container, InsuranceTakePhotoFragment())
					addToBackStack(null)
					commit()
				}
			})

			disposables.add(previewCancelButton.clicks().subscribe {
				parentFragmentManager.popBackStack()
			})

			benefitsPreviewRecyclerView.apply {
				layoutManager = LinearLayoutManager(activity)
				adapter = benefitAdapter
				addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(activity).orientation))
			}

			val dependents = insuranceViewModel.dependents
			Log.i("taaag", "found $dependents")
			if (dependents.isNotEmpty()) {
				dependentsCardView.visibility = View.VISIBLE
//				dependents_list.adapter = MemberListAdapter(context, dependents, true)
			} else {
				dependentsCardView.visibility = View.GONE
            }

		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.app_bar_search).isVisible = false
	}
}