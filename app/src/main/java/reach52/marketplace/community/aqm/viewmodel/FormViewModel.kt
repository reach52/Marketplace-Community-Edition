package reach52.marketplace.community.aqm.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.aqm.entity.*
import reach52.marketplace.community.aqm.entity.TextItem.Companion.NUMBER
import reach52.marketplace.community.aqm.repo.QuestionsRepo
import reach52.marketplace.community.aqm.utils.C

class FormViewModel : ViewModel() {

	val questions = MutableLiveData<List<Item>>()
	val answers = LinkedHashMap<String, Any?>()

	@SuppressLint("CheckResult")
	fun loadQuestions(context: Context, url: String) {

		QuestionsRepo.getQuestions(context, url).subscribe(
				{
					questions.value = it
				},
				{
					it.printStackTrace()
				}
		)

	}

	fun saveAnswers(format: Int = C.ANS_FORMAT_CODE) {

		val ques = questions.value

		ques!!.forEach {
			if (!it.optional) {
				when (it) {
					is TextItem -> {
						if (it.value.isNullOrEmpty()) {
							throw RequiredAnsNullException()
						}
					}
					is SingleSelectItem -> {
						if (it.value == null) {
							throw RequiredAnsNullException()
						}
					}
					is MultiSelectItem -> {
						if (it.value.isEmpty()) {
							throw RequiredAnsNullException()
						}
					}
					is BoolItem -> {
						if (it.value == null) {
							throw RequiredAnsNullException()
						}
					}
				}
			}
			saveAns(it, format)
		}

	}

	private fun saveAns(item: Item, format: Int) {

		val key = if (format == C.ANS_FORMAT_CODE) item.id else item.question

		when (item) {
			is TextItem -> {

				if (item.inputType == NUMBER) {
					answers[key] = item.value!!.toFloat()
				} else {
					answers[key] = item.value as String
				}
			}
			is SingleSelectItem -> {

				val ans = if (format == C.ANS_FORMAT_CODE) item.value!!.id else item.value!!.text

				answers[key] = ans

			}
			is MultiSelectItem -> {

				val ans = item.value.map {
					if (format == C.ANS_FORMAT_CODE) it.id else it.text
				}
				answers[key] = ans

			}
			is BoolItem -> {
				answers[key] = item.value!!
			}
		}

	}

	class RequiredAnsNullException() : Exception()

}