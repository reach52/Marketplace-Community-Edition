package reach52.marketplace.community.fragments.insurance

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import arrow.core.getOrElse
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.policy_owner.Beneficiaries
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_beneficiary.*
import kotlinx.android.synthetic.main.fragment_beneficiary.view.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern


class BeneficiaryFragment : Fragment() {

    var civilStatus = "Single"
    val disposables = CompositeDisposable()

    private val residentViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beneficiary, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.beneficiary)

            val maritalStatus = resources.getStringArray(R.array.civilStatus)
            spinnerCivilStatus.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    maritalStatus
            )
            spinnerCivilStatus.setSelection(getIndex(spinnerCivilStatus, civilStatus))

            val relationship = resources.getStringArray(R.array.familyMember)
            spinnerRelationship.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    relationship
            )

            disposables.add(benefitProceedButton.clicks().subscribe{

                if(contactEditText.text.toString().trim().isNotEmpty()
                        && addressEditText.text.toString().trim().isNotEmpty()
                        && fullNameEditText.text.toString().trim().isNotEmpty()){

                    if(!validEmail(emailEditText.text.toString())){
                        emailTextInputLayout.error = "Invalid email format"
                    } else {
                        val contact = contactEditText.text.toString()
                        val address = addressEditText.text.toString()
                        val fullName = fullNameEditText.text.toString()

                        if (inputChecker(contact) || contact.take(1) == " "
                                || inputChecker(fullName) || fullName.take(1) == ""
                                || inputChecker(address) || address.take(1) == ""){
                            Toast.makeText(context, "Invalid input: special characters not allowed as first input", Toast.LENGTH_LONG).show()
                        } else {
                            insuranceViewModel.addedBeneficiaries.value?.add(Beneficiaries(
                                    display = fullNameEditText.text.toString(),
                                    relationship = spinnerRelationship.selectedItem.toString(),
                                    reference = "reference" //what will this reference?
                            ))

                            TODO("add the next fragment")
                        }
                    }
                } else {
                    if(contactEditText.text.toString().trim().isEmpty()){
                        contactEditText.error = "Contact must not be empty!"
                    }
                    if(addressEditText.text.toString().trim().isEmpty()){
                        addressEditText.error = "Address must not be empty!"
                    }
                    if(fullNameEditText.text.toString().trim().isEmpty()){
                        fullNameEditText.error = "Name must not be empty!"
                    }
                }

            })

            disposables.add(benefitCancelButton.clicks().subscribe{
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        displayResident()
    }

    private fun displayResident() {
        val fullName: String
        val birthDay: String
        val gender: String
        val contact: String
        val email: String
        val address: String

        if(residentViewModel.isLogisticResident.value == true){
            val residentLogistic = insuranceViewModel.selectedLogisticResident.value
            fullName = residentLogistic!!.name
            birthDay = residentLogistic.dateOfBirth.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            gender = residentLogistic.gender
            contact = residentLogistic.contact
            email = residentLogistic.email
            address = residentLogistic.address
            civilStatus = residentLogistic.maritalStatus
        } else {
            val residentAccess = insuranceViewModel.selectedResident.value
            fullName = residentAccess!!.name
            birthDay = residentAccess.birthDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            gender = residentAccess.gender
            contact = residentAccess.contact.getOrElse { "" }
            email = residentAccess.email.getOrElse { "" }
            address = residentAccess.address.text
            civilStatus = residentAccess.maritalStatus.getOrElse { "Single" }
        }

        fullNameTextDisplay.text = fullName
        dateOfBirthTextDisplay.text = birthDay
        genderTextDisplay.text = gender
        contactEditText.setText(contact)
        emailEditText.setText(email)
        addressEditText.setText(address)
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for(i in 0 until spinner.count){
            if(spinner.getItemAtPosition(i).toString() == myString){
                return i
            }
        }
        return 0
    }

    private fun inputChecker(value : String): Boolean {
        val pattern: Pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(value.take(1))
        return matcher.find()
    }

    private fun validEmail(email: String): Boolean {
        return TextUtils.isEmpty(email) || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
