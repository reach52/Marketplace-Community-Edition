package reach52.marketplace.community.extensions.utils

import android.content.Context
import android.os.Build
import java.util.*

object LanguageUtils {

    val ENGLISH = "en"
    val KHMER = "km"
    val HILIGAYNON = "hil"
    val KANNADA = "kn"

    @Suppress("DEPRECATION")
    fun loadLocale(context: Context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            val res = context.resources
            val metrics = res.displayMetrics
            val config = res.configuration
            val language = getSavedLanguage()
            config.setLocale(Locale(language))
            res.updateConfiguration(config, metrics)
        }
    }

    fun saveLanguage(language: String) {
        SharedPrefs.write(SharedPrefs.KEY_LANGUAGE, language)
    }

    fun getSavedLanguage(): String = SharedPrefs.readString(SharedPrefs.KEY_LANGUAGE, "en")!!

    fun getSavedLanguageInISO3(): String {
        val iso2 = getSavedLanguage()
        return Locale(iso2).isO3Language
    }

}