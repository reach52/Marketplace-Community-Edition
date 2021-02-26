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
import kotlinx.android.synthetic.main.fragment_new_resident_service_interests.*

class NewResidentServiceInterestsFragment : Fragment() {

	private lateinit var vm: ResidentViewModel
	private lateinit var resident: Resident

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		vm = ViewModelProvider(activity as BaseActivity)[ResidentViewModel::class.java]
		resident = vm.resident

		return inflater.inflate(R.layout.fragment_new_resident_service_interests, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupQuestion()

		nextBtn.setOnClickListener {

			(activity as NewResidentActivity).gotoPreviewProfile()
		}

	}

	private fun setupQuestion() {

		val serviceTexts = context!!.resources.getStringArray(R.array.service_interests)
		val serviceTextCodes = context!!.resources.getStringArray(R.array.service_interests_codes)

		if (!resident.questions.containsKey(C.SER_INTEREST)) {
			resident.questions[C.SER_INTEREST] = ArrayList<String>()
		}

		val residentInterests = resident.questions[C.SER_INTEREST] as ArrayList<String>

		val serviceOptions = HashMap<String, Resident.Option>()
		for ((i, text) in serviceTexts.withIndex()) {
			val code = serviceTextCodes[i]
			serviceOptions[code] = Resident.Option(i, code, text)
		}
		val tempSer = arrayListOf<String>()
		val serDialog = AlertDialog.Builder(context!!)
				.setMultiChoiceItems(serviceTexts, null) { _, position, checked ->
					if (checked) {
						tempSer.add(serviceTextCodes[position])
					} else {
						tempSer.remove(serviceTextCodes[position])
					}
				}
				.setCancelable(false)
				.setNegativeButton(R.string.cancel) { dialog, _ ->
					dialog.dismiss()
					for (e in serviceOptions.values) {
						(dialog as AlertDialog).listView.setItemChecked(e.index, false)
					}
				}
				.setPositiveButton(R.string.save) { dialog, which ->

					residentInterests.clear()
					residentInterests.addAll(tempSer)

					if (residentInterests.isEmpty()) {
						service_answer_btn.text = getString(R.string.answer)
					} else {
						service_answer_btn.text = String.format(getString(R.string.answers_selected), residentInterests.size.toString())
					}

				}
				.create()

		if (residentInterests.isEmpty()) {
			service_answer_btn.text = getString(R.string.answer)
		} else {
			service_answer_btn.text = String.format(getString(R.string.answers_selected), residentInterests.size.toString())
		}

		service_answer_btn.setOnClickListener {
			tempSer.clear()
			tempSer.addAll(residentInterests)
			serDialog.show()
			for (temp in tempSer) {
				val index = serviceOptions[temp]!!.index
				serDialog.listView.setItemChecked(index, true)
			}
		}
	}

	override fun onResume() {
		super.onResume()
		(activity as BaseActivity).supportActionBar?.title = getString(R.string.service_interests)
	}

}