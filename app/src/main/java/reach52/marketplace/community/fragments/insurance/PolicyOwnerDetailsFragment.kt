package reach52.marketplace.community.fragments.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.insurance.InsurancePurchase
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_policy_owner_details.view.*
import org.threeten.bp.format.DateTimeFormatter


@Suppress("UNCHECKED_CAST")
class PolicyOwnerDetailsFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val insuranceViewModelGetter by lazy {
        Option.fromNullable(activity).map {
            ViewModelProvider(it)[InsuranceViewModel::class.java]
        }
    }
    private val insurancePurchase by lazy {
        InsurancePurchase(DispergoDatabase.getInstance(context!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_policy_owner_details, container, false).apply {

                insuranceViewModelGetter.map { model ->
                    if(arguments!=null){
                        val policyOwner = model.policyOwnerDocumentId.value?.let { insurancePurchase.get(it) }
                        val policyInsured = policyOwner?.insured
                        when(arguments?.get("data")){
                            "policyOwner" -> {
                                dataName.text = policyOwner?.policyOwnerFullName
                                addressDisplay.text = policyOwner?.address
                                emailDisplay.text = policyOwner?.email
                                contactDisplay.text = policyOwner?.contact
                                civilStatusDisplay.text = policyOwner?.civilStatus
                                genderDisplay.text = policyOwner?.gender
                                dateOfBirthDisplay.text = policyOwner?.dateOfBirth?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                relationshipText.visibility = View.INVISIBLE
                                relationshipDisplay.visibility = View.INVISIBLE
                            }
                            "insured" -> {
                                dataName.text = policyInsured?.display
                                addressDisplay.text = policyInsured?.address
                                emailDisplay.text = policyInsured?.email
                                contactDisplay.text = policyInsured?.contact
                                civilStatusDisplay.text = policyInsured?.civilStatus
                                genderDisplay.text = policyInsured?.gender
                                dateOfBirthDisplay.text = policyInsured?.dateOfBirth?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                relationshipDisplay.text = policyInsured?.relationship

                            }
                        }

                    }

                    when((activity as AppCompatActivity).supportActionBar?.title){
                        context.getString(R.string.insured_details) -> {
                            model.selectedPolicyInsured.observe(viewLifecycleOwner, Observer {
                                dataName.text = it.display
                                addressDisplay.text = it.address
                                emailDisplay.text = it.email
                                contactDisplay.text = it.contact
                                civilStatusDisplay.text = it.civilStatus
                                genderDisplay.text = it.gender
                                dateOfBirthDisplay.text = it.dateOfBirth.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                relationshipDisplay.text = it.relationship
                                when(it.relationship) {
                                    "" -> {
                                        relationshipDisplay.text = context.getString(R.string.self)
                                    }
                                }
                            })
                        }
                        context.getString(R.string.policy_owner_details) -> {
                            relationshipText.visibility = View.GONE
                            relationshipDisplay.visibility = View.GONE

                            model.selectedPolicyOwnerFullName.observe(viewLifecycleOwner, Observer {
                                dataName.text = it
                            })
                            model.selectedPolicyOwnerAddress.observe(viewLifecycleOwner, Observer {
                                addressDisplay.text = it
                            })
                            model.selectedPolicyOwnerEmail.observe(viewLifecycleOwner, Observer {
                                emailDisplay.text = it
                            })
                            model.selectedPolicyOwnerContact.observe(viewLifecycleOwner, Observer {
                                contactDisplay.text = it
                            })
                            model.selectedPolicyOwnerCivilStatus.observe(viewLifecycleOwner, Observer {
                                civilStatusDisplay.text = it
                            })
                            model.selectedPolicyOwnerGender.observe(viewLifecycleOwner, Observer {
                                genderDisplay.text = it
                            })
                            model.selectedPolicyOwnerDateOfBirth.observe(viewLifecycleOwner, Observer {
                                dateOfBirthDisplay.text = it.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                            })
                    }
                }
            }

            disposables.add(goBackButton.clicks().subscribe{
                parentFragmentManager.popBackStack()
            })
        }
    }

}