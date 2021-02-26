package reach52.marketplace.community.aqm.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.aqm.adapter.FormAdapter
import reach52.marketplace.community.aqm.entity.Item
import reach52.marketplace.community.aqm.entity.SingleSelectItem
import reach52.marketplace.community.aqm.utils.C.ANS_FORMAT_CODE
import reach52.marketplace.community.aqm.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : BaseActivity(), FormAdapter.OptionSelectListener {

	companion object{
		const val KEY_URL = "url"
		const val KEY_ANS_FORMAT = "ans_format"
		const val KEY_ANSWERS = "answers"
	}
	private var ansFormat =  ANS_FORMAT_CODE

	private lateinit var vm: FormViewModel
	private lateinit var adapter: FormAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_form)
		setSupportActionBar(toolbar)

		val url = intent.getStringExtra(KEY_URL)
		ansFormat = intent.getIntExtra(KEY_ANS_FORMAT, ANS_FORMAT_CODE)

		val dialog  = AlertDialog.Builder(this)
				.setMessage(getString(R.string.loading_questions))
				.create()

		vm = ViewModelProvider(this)[FormViewModel::class.java]
		vm.questions.observe(this, Observer {
			adapter = FormAdapter(this, it, this)
			form_items.adapter = adapter
			dialog.dismiss()
		})


		dialog.show()
		vm.loadQuestions(this, url)


	}

	fun onSave(v: View){

		try {

			vm.saveAnswers(ansFormat)
			setResult(Activity.RESULT_OK, Intent().apply {
				putExtra(KEY_ANSWERS, vm.answers)
			})
			finish()
		} catch (e: Exception) {
			if (e is FormViewModel.RequiredAnsNullException) {
				Toast.makeText(this, getString(R.string.mandatory_fileds), Toast.LENGTH_SHORT).show()
			} else {
				FirebaseCrashlytics.getInstance().setCustomKey("questions", vm.questions.toString())
				FirebaseCrashlytics.getInstance().setCustomKey("answers", vm.answers.toString())
				FirebaseCrashlytics.getInstance().recordException(e)
			}
		}

	}

	override fun onOptionSelect(item: SingleSelectItem, option: Item.Option) {

	}

}