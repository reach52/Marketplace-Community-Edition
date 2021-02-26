package reach52.marketplace.community.resident.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.utils.C
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.fragment_new_resident_health_profile.*

class NewResidentHealthProfileFragment : Fragment() {

	private lateinit var vm: ResidentViewModel
	private lateinit var resident: Resident

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		vm = ViewModelProvider(activity as BaseActivity)[ResidentViewModel::class.java]
		resident = vm.resident

		return inflater.inflate(R.layout.fragment_new_resident_health_profile, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupHeightAndWeight()
		setupHealthConditionQuestions()
		setupMedicationsInput()
		setupPregsQuestion()

		nextBtn.setOnClickListener {

//			fillDummyWeightAndHeight()
			val check = validateInputs()

			if (check != null) {
				Toast.makeText(context, check, Toast.LENGTH_SHORT).show()
			} else {

				resident.questions[C.HLT_WEIGHT] = weight_edittext.text.toString().toFloat()
				resident.questions[C.HLT_HEIGHT] = height_edittext.text.toString().toFloat()
				resident.questions[C.HLT_CUR_MEDS] = meds_edittext.text.toString()
				if(pregnant_answer.checkedRadioButtonId != -1) {
					resident.questions[C.HLT_PREG] = when (pregnant_answer.checkedRadioButtonId) {
						R.id.pregnant_yes -> C.HLT_PREG_YES
						R.id.pregnant_no -> C.HLT_PREG_NO
						R.id.pregnant_dna -> C.HLT_PREG_DNA
						else -> C.HLT_PREG_NA
					}
				}


				(activity as NewResidentActivity).gotoServiceInterests()
			}

		}

	}

	private fun setupHealthConditionQuestions() {

		val conditionTexts = context!!.resources.getStringArray(R.array.health_conditions)
		val conditionTextsCodes = context!!.resources.getStringArray(R.array.health_conditions_codes)

		if (!resident.questions.containsKey(C.HLT_CUR_DIA)) {
			resident.questions[C.HLT_CUR_DIA] = ArrayList<String>()
		}

		val residentConditions = resident.questions[C.HLT_CUR_DIA] as ArrayList<String>

		val conditionOptions = HashMap<String, Resident.Option>()
		for ((i, text) in conditionTexts.withIndex()) {
			val code = conditionTextsCodes[i]
			conditionOptions[code] = Resident.Option(i, code, text)
		}

		val tempConditions = arrayListOf<String>()
		val conditionsDialog = AlertDialog.Builder(context!!)
				.setMultiChoiceItems(conditionTexts, null) { _, position, checked ->
					if (checked) {
						tempConditions.add(conditionTextsCodes[position])
					} else {
						tempConditions.remove(conditionTextsCodes[position])
					}
				}
				.setCancelable(false)
				.setNegativeButton(R.string.cancel) { dialog, _ ->
					dialog.dismiss()
					for (e in conditionOptions.values) {
						(dialog as AlertDialog).listView.setItemChecked(e.index, false)
					}
				}
				.setPositiveButton(R.string.save) { dialog, which ->

					residentConditions.clear()
					residentConditions.addAll(tempConditions)

					if (residentConditions.isEmpty()) {
						condition_answer_btn.text = getString(R.string.answer)
					} else {
						condition_answer_btn.text = String.format(getString(R.string.answers_selected), residentConditions.size.toString())
					}

				}
				.create()

		if (residentConditions.isEmpty()) {
			condition_answer_btn.text = getString(R.string.answer)
		} else {
			condition_answer_btn.text =  String.format(getString(R.string.answers_selected), residentConditions.size.toString())
		}

		condition_answer_btn.setOnClickListener {
			tempConditions.clear()
			tempConditions.addAll(residentConditions)
			conditionsDialog.show()
			for (temp in tempConditions) {
				val index = conditionOptions[temp]!!.index
				conditionsDialog.listView.setItemChecked(index, true)
			}
		}

	}

	private fun setupHeightAndWeight() {

		if (!resident.questions.containsKey(C.HLT_WEIGHT)) {
			resident.questions[C.HLT_WEIGHT] = 0F
		} else {
			weight_edittext.setText(resident.questions[C.HLT_WEIGHT].toString())

		}

		if (!resident.questions.containsKey(C.HLT_HEIGHT)) {
			resident.questions[C.HLT_HEIGHT] = 0F
		} else {
			height_edittext.setText(resident.questions[C.HLT_HEIGHT].toString())
		}

	}

	private fun setupMedicationsInput() {

		if (!resident.questions.containsKey(C.HLT_CUR_MEDS)) {
			resident.questions[C.HLT_CUR_MEDS] = ""
		} else {
			meds_edittext.setText((resident.questions[C.HLT_CUR_MEDS] as String))
		}

	}

	private fun setupPregsQuestion(){

		if (resident.questions.containsKey(C.HLT_PREG)) {

			when (resident.questions[C.HLT_PREG] as String) {
				C.HLT_PREG_YES -> pregnant_yes.isChecked = true
				C.HLT_PREG_NO -> pregnant_no.isChecked = true
				C.HLT_PREG_DNA -> pregnant_dna.isChecked = true
				else -> pregnant_na.isChecked = true

			}

		}

	}

	private fun validateInputs(): String? {

		try {
			val weight = weight_edittext.text.toString().toDouble()
			if (weight < 15) {
				return getString(R.string.weight_too_low)
			}
			if (weight > 700) {
				return getString(R.string.weight_too_heavy)
			}
			if (weight_edittext.text.first().toString() == "0"){
				return getString(R.string.first_weight_input_not_0)
			}
		} catch (e: Exception) {
			return getString(R.string.invalid_weight)
		}


		try {
			val height = height_edittext.text.toString().toDouble()
			if (height < 30) {
				return getString(R.string.height_too_low)
			}
			if (height > 230) {
				return getString(R.string.height_too_high)
			}

			if (height_edittext.text.first().toString() == "0"){
				return getString(R.string.first_height_must_not_0)
			}
		} catch (e: Exception) {
			return getString(R.string.invalid_height)
		}

		return null

	}

	override fun onResume() {
		super.onResume()
		(activity as BaseActivity).supportActionBar?.title = getString(R.string.health_profile)
	}

	private fun fillDummyWeightAndHeight() {
		if (BuildConfig.BUILD_TYPE == "debug") {
			weight_edittext.setText("100")
			height_edittext.setText("100")
		}
	}

}