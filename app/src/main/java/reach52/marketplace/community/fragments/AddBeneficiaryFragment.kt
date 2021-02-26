package reach52.marketplace.community.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.CoverageInsured
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_family_member.view.*

class AddBeneficiaryFragment : Fragment() {
    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val disposables = CompositeDisposable()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_family_member, container, false).apply {
            val familyMember = resources.getStringArray(R.array.familyMember)

            spinnerFamilyMember.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    familyMember)

            disposables.add(addFamilyButton.clicks().subscribe {
                val beneficiaryName = editTextFullName.text.toString()
                val beneficiaryContact = editTextContact.text.toString()
                if (beneficiaryName.isEmpty() || beneficiaryContact.isEmpty()) {
                    Toast.makeText(context, getString(R.string.text_required_fields), Toast.LENGTH_LONG).show()
                } else {

                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                    insuranceViewModel.coveredFamilyMembers.value?.add(CoverageInsured(
                            beneficiary = beneficiaryName,
                            dependent = beneficiaryContact,
                            relationship = spinnerFamilyMember.selectedItem.toString()
                    ))
                    parentFragmentManager
                            .popBackStack()
                }
            })
        }

    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}