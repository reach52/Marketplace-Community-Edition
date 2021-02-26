package reach52.marketplace.community.resident.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.extensions.utils.getRandomEmail
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_resident.*
import kotlinx.android.synthetic.main.fragment_add_resident.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.Exception
import java.util.*

@Suppress("UNCHECKED_CAST")
class AddResidentFragment : Fragment() {

	private val disposables = CompositeDisposable()
	private val countryCode = CountryManager.getCountryCode()
	private val countryCodeCount = CountryManager().getCountryPhoneCode(countryCode).length

	private lateinit var vm: ResidentViewModel
	private lateinit var ctx: Context

	override fun onAttach(context: Context) {
		super.onAttach(context)
		this.ctx = context
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	@ExperimentalUnsignedTypes
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		vm = ViewModelProvider(activity as BaseActivity)[ResidentViewModel::class.java]

		return inflater.inflate(R.layout.fragment_add_resident, container, false).apply {

			countryCodeTextView.text = CountryManager().getCountryPhoneCode(countryCode)

			val civilStatusArray = resources.getStringArray(R.array.civilStatus)
			spinnerMaritalStatus.adapter = ArrayAdapter(
					context!!,
					android.R.layout.simple_spinner_dropdown_item,
					civilStatusArray
			)

			radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
				val radio: RadioButton = findViewById(checkedId)
				vm.resident.gender = radio.text.toString()
			}

			disposables.add(editTextDateOfBirth.clicks().subscribe {
				val now = Calendar.getInstance()
				val datePicker = DatePickerDialog(context,
						{ _, year, month, dayOfMonth ->
							val localDate: LocalDate = LocalDate.of(year, month + 1, dayOfMonth)
							val localTime: LocalTime =
									LocalTime.of(0, 0, 0, 0)

							vm.resident.dob = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC)
							editTextDateOfBirth.setText(
									vm.resident.dob.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
							)
						},
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH))
				datePicker.datePicker.maxDate = System.currentTimeMillis()
				datePicker.show()
			})



			// code for set text on button 21-12-2020
			try {
				if (vm.update) {
					nextBtn.setText(getString(R.string.Update_resident))
				} else {
					nextBtn.setText(getString(R.string.add_new_resident))
				}
			}catch (e:Exception)
			{
				System.out.println(""+e.message)
			}

			nextBtn.setOnClickListener {
				clearErrorText()
				loadResidentDataFromInputs(this)
//				fillUpDummyData()
				if(textValidationInput() == 0){
//					if (countryCode == CountryCode.IND) {
						(activity as NewResidentActivity).gotoInsuranceProfile()
//					} else {
//						(activity as NewResidentActivity).gotoPreviewProfile()
//					}
				}
//				(activity as NewResidentActivity).gotoInsuranceProfile()
			}

		}
	}

	private fun fillUpDummyData() {

		if (BuildConfig.BUILD_TYPE == "debug") {

			val resident = vm.resident
			resident.firstName = "First_${Random().nextInt(1000)}"
			resident.lastName = "Last_${Random().nextInt(1000)}"
			resident.email = getRandomEmail()
			resident.maritalStatus = "Married"
			resident.phone = CountryManager().getCountryPhoneCode(countryCode) + contactEditText.text.toString()
			resident.dob = ZonedDateTime.now(ZoneOffset.UTC).minusYears(40)
			resident.addressLine = "111 Block, ABC Street"
			resident.city = "Paris"
			resident.zipCode = "900233"
			resident.gender = "Female"
			resident.isoCountry = countryCode
			resident.country = CountryManager.getCountry(resident.isoCountry)!!

		}

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		if (vm.update) {
			fillUIWithValues()
		}

	}

	private fun loadResidentDataFromInputs(view: View) {
		val resident = vm.resident
		resident.firstName = firstNameEditText.text.toString()
		resident.middleName = middleNameEditText.text.toString()
		resident.lastName = lastNameEditText.text.toString()
		resident.email = residentEmailEditText.text.toString()
		resident.maritalStatus = spinnerMaritalStatus.selectedItem.toString()
		resident.phone = CountryManager().getCountryPhoneCode(countryCode) + contactEditText.text.toString()
		resident.addressLine = addressEditText.text.toString()
		resident.city = cityInputEditText.text.toString()
		resident.zipCode = postalCodeEditText.text.toString()
		if(maleRadioButton.isChecked || femaleRadioButton.isChecked){
			resident.gender = view.findViewById<RadioButton>(radioGroupGender.checkedRadioButtonId).text.toString()
		}else{
			Toast.makeText(context, "Please select gender", Toast.LENGTH_SHORT).show()
		}
		resident.isoCountry = countryCode
		resident.country = CountryManager.getCountry(resident.isoCountry)!!


	}

	override fun onResume() {
		super.onResume()
		if (vm.update) {
			(activity as BaseActivity).supportActionBar?.title = getString(R.string.Update_resident)
		} else {
			(activity as BaseActivity).supportActionBar?.title = context!!.getString(R.string.add_new_resident)
		}
	}

	override fun onDestroy() {
		disposables.clear()
		super.onDestroy()
	}

	private fun residentValidEmail(email: String): Boolean {
		return TextUtils.isEmpty(email) || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
	}

	private fun fillUIWithValues() {

		val resident = vm.resident
		firstNameEditText.setText(resident.firstName)
		middleNameEditText.setText(resident.middleName)
		lastNameEditText.setText(resident.lastName)
		contactEditText.setText(resident.phone.removeRange(0, countryCodeCount))
		residentEmailEditText.setText(resident.email)
		addressEditText.setText(resident.addressLine)
		cityInputEditText.setText(resident.city)
		postalCodeEditText.setText(resident.zipCode)
		editTextDateOfBirth.setText(resident.dob.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))

		for (i in 0 until spinnerMaritalStatus.count) {
			if (spinnerMaritalStatus.getItemAtPosition(i).toString() == resident.maritalStatus) {
				spinnerMaritalStatus.setSelection(i)
				break
			}
		}


		if (resident.gender == getString(R.string.male)) {
			resident.gender = maleRadioButton.text.toString()
			maleRadioButton.isChecked = true
		} else {
			resident.gender = femaleRadioButton.text.toString()
			femaleRadioButton.isChecked = true
		}

	}

	private fun textValidationInput(): Int {
		var errorCount = 0
		val resident = vm.resident
		val phoneLength = resident.phone.removeRange(0, countryCodeCount).length

//		if(!resident.firstName.matches("[a-zA-Z- ]+".toRegex())){
//			firstNameTextInputLayout.error = getString(R.string.invalid_input)
//			errorCount = 1
//		}

		if (resident.firstName.isEmpty()) {
			firstNameTextInputLayout.error = getString(R.string.first_name_cannot_be_empty)
			errorCount = 1
		}

//		if(resident.middleName != ""){
//
//			if(!resident.middleName!!.matches("[a-zA-Z- ]+".toRegex())){
//				middleNameTextInputLayout.error = getString(R.string.invalid_input)
//				errorCount = 1
//			}
//		}

//		if(!resident.lastName.matches("[a-zA-Z- ]+".toRegex())){
//			lastNameTextInputLayout.error = getString(R.string.invalid_input)
//			errorCount = 1
//		}

		if (resident.lastName.isEmpty()) {
			lastNameTextInputLayout.error = getString(R.string.last_name_cannot_be_empty)
			errorCount = 1
		}

		if (resident.email != "") {
			if (!residentValidEmail(resident.email!!)) {
				emailTextInputLayout.error = getString(R.string.invalid_email)
				errorCount = 1
			}
		}

		if (resident.addressLine.isEmpty()) {
			addressTextInputLayout.error = getString(R.string.address_is_incomplete)
			errorCount = 1
		}

		if (resident.zipCode.isEmpty()) {
			postalCodeInputTextLayout.error = getString(R.string.postal_code_cannot_be_empty)
			errorCount = 1
		}

//		if(!resident.city.matches("[a-zA-Z- ]+".toRegex())){
//			cityTextInputLayout.error = "Invalid input"
//			errorCount = 1
//		}

		if (resident.city.isEmpty()) {
			cityTextInputLayout.error = getString(R.string.city_cannot_be_empty)
			errorCount = 1
		}

		if (!vm.resident.isDobInitialized()) {
			dobTextInputLayout.error = getString(R.string.dob_not_set)
			errorCount = 1
		}

		if (resident.phone.removeRange(0, countryCodeCount) != "") {
			if (phoneLength < 10) {
				contactTextInputLayout.error = getString(R.string.phone_min_limit)
				errorCount = 1
			}

			if (!resident.phone.matches("[+][0-9]+".toRegex())) {
				contactTextInputLayout.error = getString(R.string.invalid_phone_number)
				errorCount = 1
			}

		} else {
			contactTextInputLayout.error = getString(R.string.phone_number_cannot_be_empty)
			errorCount = 1
		}

//		if(errorCount != 0){
//			Toast.makeText(context, "Invalid: Please review the form and try again", Toast.LENGTH_SHORT).show()
//		}
		return errorCount
	}

	private fun clearErrorText() {
		firstNameTextInputLayout.error = null
		lastNameTextInputLayout.error = null
		emailTextInputLayout.error = null
		cityTextInputLayout.error = null
		postalCodeInputTextLayout.error = null
		addressTextInputLayout.error = null
		dobTextInputLayout.error = null
		contactTextInputLayout.error = null
	}
}
