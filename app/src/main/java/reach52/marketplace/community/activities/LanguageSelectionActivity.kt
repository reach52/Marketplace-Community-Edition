package reach52.marketplace.community.activities

import android.os.Bundle
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.extensions.utils.LanguageUtils.ENGLISH
import reach52.marketplace.community.extensions.utils.LanguageUtils.HILIGAYNON
import reach52.marketplace.community.extensions.utils.LanguageUtils.KANNADA
import reach52.marketplace.community.extensions.utils.LanguageUtils.KHMER
import kotlinx.android.synthetic.main.activity_language_selection.*


class LanguageSelectionActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_language_selection)

		english_btn.setOnClickListener {
			onLanguageSelect(ENGLISH)
		}
		khmer_btn.setOnClickListener {
			onLanguageSelect(KHMER)
		}
		hiligaynon_btn.setOnClickListener{
			onLanguageSelect(HILIGAYNON)
		}

		btnKannada.setOnClickListener { onLanguageSelect(KANNADA) }

	}

	private fun onLanguageSelect(language: String) {
		LanguageUtils.saveLanguage(language)
		recreate()
//		overridePendingTransition(0, 0)
		finish()
//		startActivity(Intent(this, LoginActivity::class.java))
	}

}