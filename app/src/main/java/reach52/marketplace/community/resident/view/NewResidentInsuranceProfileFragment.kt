package reach52.marketplace.community.resident.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.utils.C
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.fragment_new_resident_insurance_profile.*


class NewResidentInsuranceProfileFragment : Fragment() {

	private lateinit var vm: ResidentViewModel
	private lateinit var resident: Resident

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		vm = ViewModelProvider(activity as BaseActivity)[ResidentViewModel::class.java]
		resident = vm.resident

		return inflater.inflate(R.layout.fragment_new_resident_insurance_profile, container, false)


	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupFirstQuestion()

		setupSecondQuestion()

		setupThirdQuestion()
		setupFourthQuestion()

		nextBtn.setOnClickListener {

			if (bank_acc_answer.checkedRadioButtonId != -1) {
				resident.questions[C.INS_BANK_ACC] = bank_acc_answer.checkedRadioButtonId == R.id.bank_acc_yes
			}
			if (e_payment_answer.checkedRadioButtonId != -1) {
				resident.questions[C.INS_EPAY] = e_payment_answer.checkedRadioButtonId == R.id.e_payment_yes
			}

			(activity as NewResidentActivity).gotoHealthProfile()
		}

	}

	private fun setupFirstQuestion() {

		val existingInsuranceTexts = context!!.resources.getStringArray(R.array.existing_insurance_options)
		val existingInsuranceTextsCodes = context!!.resources.getStringArray(R.array.existing_insurance_options_codes)

		if (!resident.questions.containsKey(C.INS_STATUS)) {
			resident.questions[C.INS_STATUS] = ArrayList<String>()
		}

		val residentInsStatus = resident.questions[C.INS_STATUS] as ArrayList<String>

		val eIOptions = HashMap<String, Resident.Option>()
		for ((i, text) in existingInsuranceTexts.withIndex()) {
			val code = existingInsuranceTextsCodes[i]
			eIOptions[code] = Resident.Option(i, code, text)
		}
		val tempEI = arrayListOf<String>()
		val eIDialog = AlertDialog.Builder(context!!)
				.setMultiChoiceItems(existingInsuranceTexts, null) { _, position, checked ->
					if (checked) {
						tempEI.add(existingInsuranceTextsCodes[position])
					} else {
						tempEI.remove(existingInsuranceTextsCodes[position])
					}
				}
				.setCancelable(false)
				.setNegativeButton(R.string.cancel) { dialog, _ ->
					dialog.dismiss()
					for (e in eIOptions.values) {
						(dialog as AlertDialog).listView.setItemChecked(e.index, false)
					}
				}
				.setPositiveButton(R.string.save) { dialog, which ->

					residentInsStatus.clear()
					residentInsStatus.addAll(tempEI)

					if (residentInsStatus.isEmpty()) {
						existin_insurance_btn.text = getString(R.string.answer)
					} else {
						existin_insurance_btn.text = residentInsStatus.size.toString() + " " + getString(R.string.answers_selected)
					}

				}
				.create()

		if (residentInsStatus.isEmpty()) {
			existin_insurance_btn.text = getString(R.string.answer)
		} else {
			existin_insurance_btn.text = String.format(getString(R.string.answers_selected), residentInsStatus.size.toString())
		}

		existin_insurance_btn.setOnClickListener {
			tempEI.clear()
			tempEI.addAll(residentInsStatus)
			eIDialog.show()
			for (temp in tempEI) {

				val index = eIOptions[temp]!!.index
				eIDialog.listView.setItemChecked(index, true)

			}
		}
	}

	private fun setupSecondQuestion() {
		val futureInsuranceTexts = context!!.resources.getStringArray(R.array.future_insurance_options)
		val futureInsuranceTextsCodes = context!!.resources.getStringArray(R.array.future_insurance_options_codes)

		if (!resident.questions.containsKey(C.INS_INTEREST)) {
			resident.questions[C.INS_INTEREST] = ArrayList<String>()
		}
		val residentInsInterest = resident.questions[C.INS_INTEREST] as ArrayList<String>

		val fIOptions = HashMap<String, Resident.Option>()
		for ((i, text) in futureInsuranceTexts.withIndex()) {
			val code = futureInsuranceTextsCodes[i]
			fIOptions[code] = Resident.Option(i, code, text)
		}
		val tempFI = arrayListOf<String>()
		val fIDialog = AlertDialog.Builder(context!!)
				.setMultiChoiceItems(futureInsuranceTexts, null) { _, position, checked ->
					if (checked) {
						tempFI.add(futureInsuranceTextsCodes[position])
					} else {
						tempFI.remove(futureInsuranceTextsCodes[position])
					}
				}
				.setCancelable(false)
				.setNegativeButton(R.string.cancel) { dialog, _ ->
					dialog.dismiss()
					for (e in fIOptions.values) {
						(dialog as AlertDialog).listView.setItemChecked(e.index, false)
					}
				}
				.setPositiveButton(R.string.save) { dialog, which ->

					residentInsInterest.clear()
					residentInsInterest.addAll(tempFI)

					if (residentInsInterest.isEmpty()) {
						future_insurance_btn.text = getString(R.string.answer)
					} else {
						future_insurance_btn.text = String.format(getString(R.string.answers_selected), residentInsInterest.size.toString())
					}

				}
				.create()

		if (residentInsInterest.isEmpty()) {
			future_insurance_btn.text = getString(R.string.answer)
		} else {
			future_insurance_btn.text = String.format(getString(R.string.answers_selected), residentInsInterest.size.toString())
		}

		future_insurance_btn.setOnClickListener {
			tempFI.clear()
			tempFI.addAll(residentInsInterest)
			fIDialog.show()
			for (temp in tempFI) {

				val index = fIOptions[temp]!!.index
				fIDialog.listView.setItemChecked(index, true)
			}
		}
	}

	private fun setupThirdQuestion() {

		if (resident.questions.containsKey(C.INS_BANK_ACC)) {

			val hasBank = resident.questions[C.INS_BANK_ACC] as Boolean
			if (hasBank) {
				bank_acc_yes.isChecked = true
			} else {
				bank_acc_no.isChecked = true
			}
		}

	}

	private fun setupFourthQuestion() {

		if (resident.questions.containsKey(C.INS_EPAY)) {
			val hasBank = resident.questions[C.INS_EPAY] as Boolean
			if (hasBank) {
				e_payment_yes.isChecked = true
			} else {
				e_payment_no.isChecked = true
			}
		}

	}

	override fun onResume() {
		super.onResume()
		(activity as BaseActivity).supportActionBar?.title = getString(R.string.insurance_profile)
	}

}