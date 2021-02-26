package reach52.marketplace.community.signup.view

import android.os.Bundle
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.extensions.utils.fromHtml
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.toolbar.*

class FAQActivity : BaseActivity() {

	companion object {
		const val KEY_MODE = "mode"
		const val KEY_FAQ = 1
		const val KEY_TnC = 2
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_faq)

		val mode = intent.getIntExtra(KEY_MODE, KEY_FAQ)

		when (mode) {
			KEY_TnC -> {
				toolbar_heading.text = ""
				val html = getString(R.string.TnC)
				faq_content.text = fromHtml(html)
			}

			KEY_FAQ -> {

				toolbar_heading.text = getString(R.string.faq_heading)
				val html = getString(R.string.faq)
				faq_content.text = fromHtml(html)

			}
		}

		back_btn.setOnClickListener{
			super.onBackPressed()
		}

	}
}