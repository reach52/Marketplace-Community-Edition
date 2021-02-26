package reach52.marketplace.community.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_other_details.view.*

class OtherDetailsFragment : Fragment() {
    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val disposables = CompositeDisposable()

    @ExperimentalUnsignedTypes
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_details, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.addtional_detail_info)

            val civilStatus = resources.getStringArray(R.array.civilStatus)
            civilStatusSpinner.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    civilStatus
            )

            disposables.add(proceedButton.clicks().subscribe {
                if (isValidEmail(emailEditText.text.toString()) || (emailEditText.text.toString() == "")) {
                    insuranceViewModel.selectedResident.value = insuranceViewModel.selectedResident.value?.copy(
                            maritalStatus = Option.fromNullable(civilStatusSpinner.selectedItem.toString()),
                            email = Option.fromNullable(emailEditText.text.toString()),
                            contact = Option.fromNullable(contactEditText.text.toString())
                    )

                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                    parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, InsuranceConsentFragment())
                            .addToBackStack(null)
                            .commit()
                } else {
                    Toast.makeText(context, "Check the format of your email and your contact number", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}