package reach52.marketplace.community.extensions.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*

class MyContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {

        @Suppress("DEPRECATION")
        fun wrap(context: Context, language: String): MyContextWrapper {
            val config = context.resources.configuration
            val sysLocale =
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
                        config.locales[0]
                    else
                        config.locale

            if (language.isNotEmpty() && sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.setLocale(locale)
                } else {
                    config.locale = locale
                }
            }

            val isAboveAPI24 = Build.VERSION.SDK_INT > Build.VERSION_CODES.N

            val uContext = if (isAboveAPI24) context.createConfigurationContext(config) else context

            if (!isAboveAPI24) {
                uContext.resources.updateConfiguration(config, uContext.resources.displayMetrics)
            }

            return MyContextWrapper(uContext)
        }
    }
}