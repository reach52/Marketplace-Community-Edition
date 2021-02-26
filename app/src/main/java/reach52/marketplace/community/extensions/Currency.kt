package reach52.marketplace.community.extensions

import java.text.DecimalFormat
import java.util.*

fun Double.toPhilippinesCurrency(): String = DecimalFormat.getCurrencyInstance(Locale("en", "PH")).format(this)

// TODO: 6/16/2020 try to work this out. figure a way to translate currency for different country
//fun Double.toCambodiaCurrency(): String = DecimalFormat.getCurrencyInstance(Locale("kh", "KH")).format(this)
fun Double.toCambodiaCurrency(): String = DecimalFormat.getCurrencyInstance(Locale("en", "KH")).format(this)