package reach52.marketplace.community.fragments.follow_up

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.follow_up.FollowUp
import reach52.marketplace.community.models.follow_up.FollowUpResident
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.FollowUpViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_follow_up_details.*
import kotlinx.android.synthetic.main.fragment_follow_up_details.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class FollowUpDetailsFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private lateinit var dateFollowUp: ZonedDateTime

    private val followUpViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[FollowUpViewModel::class.java]
        }?: throw Exception("Invalid Activity")
    }

    private val followUpResident by lazy {
        FollowUpResident(DispergoDatabase.getInstance(context!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        followUpViewModel.selectedFollowUp.value =
                followUpResident.get(followUpViewModel.selectedFollowId.value.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follow_up_details, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.follow_up_details)

            val followUp = followUpViewModel.selectedFollowUp.value
            dateFollowUp = followUp!!.dateFollowUp

            if(followUpViewModel.isAllFollowUpList.value == true){
                hideActionBar()
            }

            if(followUp.status == "done"){
                markDoneCheckBox.isChecked = true
            }

            disposables.add(backButton.clicks().subscribe(){
                parentFragmentManager.popBackStack()
            })

            disposables.add(editFollowUpButton.clicks().subscribe {
                followUpProductTextInputLayout.visibility = View.VISIBLE
                followUpDateTextInputLayout.visibility = View.VISIBLE
                followUpReasonText.visibility = View.VISIBLE
                spinnerFollowUpReason.visibility = View.VISIBLE
                followUpDateText.visibility = View.GONE
                descriptionText.visibility = View.GONE
                reasonText.visibility = View.GONE
                followUpdateTextDisplay.visibility = View.GONE
                productTextDisplay.visibility = View.GONE
                reasonTextDisplay.visibility = View.GONE
                editFollowUpButton.visibility = View.GONE
                cardView3.visibility = View.GONE
                addNotesText.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
                saveEditedFollowUpButton.visibility = View.VISIBLE
                markDoneCheckBox.visibility = View.GONE
                backButton.visibility = View.GONE

                val followUpReason = resources.getStringArray(R.array.follow_up_reason)
                spinnerFollowUpReason.adapter = ArrayAdapter(
                        context!!,
                        android.R.layout.simple_spinner_dropdown_item,
                        followUpReason
                )
                spinnerFollowUpReason.setSelection(getIndex(spinnerFollowUpReason, followUp.reason))

                disposables.add(followUpDateEditText.clicks().subscribe{
                    val now = Calendar.getInstance()
                    val datePicker = DatePickerDialog(context,
                            DatePickerDialog.OnDateSetListener { _, year, month , dayOfMonth ->
                                val localDate : LocalDate = LocalDate.of(year, month + 1, dayOfMonth)
                                val localTime : LocalTime = LocalTime.of(0,0,0,0)

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
            })

            disposables.add(cancelButton.clicks().subscribe {
                hideDetailsText()
            })

            disposables.add(markDoneCheckBox.clicks().subscribe {
                val notes = notesEditText.text.toString()

                if(notesEditText.text.toString().trim().isNotEmpty()){
                    if(inputChecker(notes) || notes.take(1) == " "){
                        Toast.makeText(context, context.getString(R.string.special_character_message), Toast.LENGTH_LONG).show()
                    } else {
                        if(markDoneCheckBox.isChecked){
                            Toast.makeText(context, context.getString(R.string.mark_down), Toast.LENGTH_LONG).show()
                            followUpResident.markFollowUp(
                                    followUpId = followUpViewModel.selectedFollowId.value.toString(),
                                    status = "done"
                            )
                        } else {
                            Toast.makeText(context, context.getString(R.string.unmarked), Toast.LENGTH_LONG).show()
                            followUpResident.markFollowUp(
                                    followUpId = followUpViewModel.selectedFollowId.value.toString(),
                                    status = "active"
                            )
                        }
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.folllow_up_chicking), Toast.LENGTH_SHORT).show()
                    markDoneCheckBox.isChecked = false
                }
            })

            disposables.add(saveNotesButton.clicks().subscribe {

                val notes = notesEditText.text.toString()

                if(notesEditText.text.toString().trim().isNotEmpty()){
                    if(inputChecker(notes) || notes.take(1) == " "){
                        Toast.makeText(context, context.getString(R.string.special_character_message), Toast.LENGTH_LONG).show()
                    } else {
                        followUpResident.addFollowUpNotes(
                                followUpId = followUpViewModel.selectedFollowId.value.toString(),
                                notes = notesEditText.text.toString()
                        )
                        Toast.makeText(context, context.getString(R.string.successfully_saved), Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(context, context.getString(R.string.add_notes), Toast.LENGTH_SHORT).show()
                }
            })

            disposables.add(saveEditedFollowUpButton.clicks().subscribe{

                val productText = followUpProductEditText.text.toString()

                if(followUpDateEditText.text.toString().isNotEmpty() && followUpProductEditText.text.toString().isNotEmpty()){
                    if(inputChecker(productText) || productText.take(1) == " "){
                        Toast.makeText(context, context.getString(R.string.special_character_message), Toast.LENGTH_LONG).show()
                    } else {
                        followUpResident.updateFollowUp(
                                followUpId = followUpViewModel.selectedFollowId.value.toString(),
                                dateFollowUp = dateFollowUp,
                                productDescription = followUpProductEditText.text.toString(),
                                reason = spinnerFollowUpReason.selectedItem.toString()
                        )
                        productTextDisplay.text = followUpProductEditText.text.toString()
                        followUpdateTextDisplay.text = dateFollowUp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                        reasonTextDisplay.text = spinnerFollowUpReason.selectedItem.toString()
                        hideDetailsText()
                        Toast.makeText(context, context.getString(R.string.successfully_updated), Toast.LENGTH_LONG).show()
                    }
                } else {
                    if(followUpDateEditText.text.toString().isEmpty()){
                        followUpDateTextInputLayout.error = context.getString(R.string.follow_up_date_validation)
                    }

                    if(followUpProductEditText.text.toString().isEmpty()){
                        followUpProductTextInputLayout.error = context.getString(R.string.input_products)
                    }
                }

            })

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        displayFollowUp(followUpResident.get(followUpViewModel.selectedFollowId.value.toString()))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    private fun displayFollowUp(followUp: FollowUp) {
        followUpProductEditText.setText(followUp.productDescription)
        followUpDateEditText.setText(followUp.dateFollowUp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))

        residentNameTextDisplay.text = followUp.residentName
        addressTextDisplay.text = followUp.address
        productTextDisplay.text = followUp.productDescription
        followUpdateTextDisplay.text = followUp.dateFollowUp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        contactTextDisplay.text = followUp.contact
        reasonTextDisplay.text = followUp.reason
        notesEditText.setText(followUp.notes)
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

    private fun hideDetailsText(){
        followUpProductTextInputLayout.visibility = View.GONE
        followUpDateTextInputLayout.visibility = View.GONE
        followUpReasonText.visibility = View.GONE
        spinnerFollowUpReason.visibility = View.GONE
        followUpDateText.visibility = View.VISIBLE
        descriptionText.visibility = View.VISIBLE
        reasonText.visibility = View.VISIBLE
        followUpdateTextDisplay.visibility = View.VISIBLE
        productTextDisplay.visibility = View.VISIBLE
        reasonTextDisplay.visibility = View.VISIBLE
        editFollowUpButton.visibility = View.VISIBLE
        cardView3.visibility = View.VISIBLE
        addNotesText.visibility = View.VISIBLE
        cancelButton.visibility = View.GONE
        saveEditedFollowUpButton.visibility = View.GONE
        backButton.visibility = View.VISIBLE
        markDoneCheckBox.visibility = View.VISIBLE
    }

    private fun hideActionBar() {
        val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.navigationIcon = null
    }
}
