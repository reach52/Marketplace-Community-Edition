package reach52.marketplace.community.fragments.follow_up

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.follow_up.FollowUpResident
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.FollowUpViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_follow_up.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class AddFollowUpFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private lateinit var dateFollowUp: ZonedDateTime
    private lateinit var residentId: String
    private lateinit var residentName: String
    private lateinit var address: String
    private lateinit var email : String
    private lateinit var contact : String

    private val followUpViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[FollowUpViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

//    private val user by lazy {
//        Auth.currentSession(context!!).map { it.user }
//    }

    private val followUp by lazy {
        FollowUpResident(DispergoDatabase.getInstance(context!!))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_follow_up, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.add_follow_up_details)

            if(followUpViewModel.isLogisticResident.value == true){
                val resident = followUpViewModel.selectedLogisticResident.value
                residentId = resident?.residentId.toString()
                residentName = resident?.name.toString()
                address = resident?.address.toString()
                email = resident?.email.toString()
                contact = resident?.contact.toString()
                contactEditText.setText(contact)
                emailEditText.setText(email)
            } else {
                val resident = followUpViewModel.selectedResident.value
                residentId = resident?.id.toString()
                residentName = resident?.name.toString()
                email = resident?.email.toString()
                contact = resident?.contact.toString()
                address = resident?.address!!.text
            }

            disposables.add(followUpDateEditText.clicks().subscribe{
                val now = Calendar.getInstance()
                val datePicker = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, month , dayOfMonth ->
                    val localDate: LocalDate = LocalDate.of(year, month + 1, dayOfMonth)
                    val localTime: LocalTime =
                            LocalTime.of(0, 0, 0, 0)

                    dateFollowUp = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC)
                    followUpDateEditText.setText(
                            dateFollowUp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                    )
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH))
                datePicker.datePicker.minDate = System.currentTimeMillis()
                datePicker.show()
            })

            val followUpReason = resources.getStringArray(R.array.follow_up_reason)
            spinnerFollowUpReason.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    followUpReason
            )

            disposables.add(buttonSaveFollowUp.clicks().subscribe {
                if(followUpDateEditText.text.toString().isNotEmpty() && contactEditText.text.toString().isNotEmpty() && followUpProductEditText.text.toString().isNotEmpty()){
                    if (!validEmail(emailEditText.text.toString())) {
                        emailTextInputLayout.error = context.getString(R.string.invalid_email)
                    } else {
                        val productText = followUpProductEditText.text.toString()
                        if(inputChecker(productText) || productText.take(1) == " ") {
                            Toast.makeText(context, context.getString(R.string.invalid_input), Toast.LENGTH_LONG).show()
                        } else {
//                            followUp.new(
//                                    user = user,
//                                    residentId = residentId,
//                                    residentName = residentName,
//                                    address = address,
//                                    email = emailEditText.text.toString(),
//                                    contact = contactEditText.text.toString(),
//                                    productDescription = followUpProductEditText.text.toString(),
//                                    reason = spinnerFollowUpReason.selectedItem.toString(),
//                                    dateFollowUp = dateFollowUp
//                            )
                            followUpViewModel.resetFollowUpVM()
                            parentFragmentManager.popBackStack()
                        }
                    }
                } else {
                    if(followUpDateEditText.text.toString().isEmpty()){
                        followUpDateTextInputLayout.error = context.getString(R.string.date_validation)
                    }

                    if(contactEditText.text.toString().isEmpty()){
                        contactTextInputLayout.error = context.getString(R.string.phone_number_validation)
                    }

                    if(followUpProductEditText.text.toString().isEmpty()){
                        followUpProductTextInputLayout.error = context.getString(R.string.product_validation)
                    }
                }
            })

            disposables.add(btnCancelFollowUp.clicks().subscribe {
                parentFragmentManager.popBackStack()
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    private fun validEmail(email: String): Boolean {
        return TextUtils.isEmpty(email) || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun inputChecker(value : String): Boolean {
        val pattern: Pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(value.take(1))
        return matcher.find()
    }
}
