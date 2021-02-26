package reach52.marketplace.community.extensions.utils

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.telephony.PhoneNumberUtils
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.util.Patterns
import reach52.marketplace.community.DispergoApp
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

fun dip(px: Int): Int {

	val dpi = DispergoApp.get().resources.configuration.densityDpi
	return px * (dpi / 160)

}


fun fromHtml(html: String, bulletRadius: Int = 3, gapWidth: Int = 8, drawable: Drawable? = null): SpannableStringBuilder {
	@Suppress("DEPRECATION")
	val htmlSpannable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
	} else {
		Html.fromHtml(html, null, Html.TagHandler { opening, tag, output, xmlReader ->
			if (tag == "ul" && !opening) output.append("\n")
			if (tag == "li" && opening) output.append("\n\t• ")
		})
	}
	val spannableBuilder = SpannableStringBuilder(htmlSpannable)
	val bulletSpans = spannableBuilder.getSpans(0, spannableBuilder.length, BulletSpan::class.java)
	bulletSpans.forEach {
		val start = spannableBuilder.getSpanStart(it)
		val end = spannableBuilder.getSpanEnd(it)
		spannableBuilder.removeSpan(it)
		spannableBuilder.setSpan(
				CustomBulletSpan(bulletRadius = dip(bulletRadius), gapWidth = dip(gapWidth), drawable = drawable),
				start,
				end,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE
		)
	}
	return spannableBuilder
}

fun calculateAgeFromDOB(dob: ZonedDateTime): Int {
	var dobStr = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(dob)
	dobStr = dobStr.split("T")[0]
	return calculateAgeFromDOB(dobStr)
}

fun calculateAgeFromDOB(dobStr: String): Int {

	val dob = Calendar.getInstance()
	val today: Calendar = Calendar.getInstance()

	val strs = dobStr.split("-")
	val y = strs[0].toInt()
	val m = strs[1].toInt()
	val d = strs[2].toInt()
	dob.set(y, m, d)

	var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

	if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
		age--
	}

	return age

}

fun createDatePicker(context: Context, listener: DatePickerDialog.OnDateSetListener): DatePickerDialog {

	val cal = Calendar.getInstance()
	return DatePickerDialog(context,
			listener,
			cal.get(Calendar.YEAR),
			cal.get(Calendar.MONTH),
			cal.get(Calendar.DAY_OF_MONTH)
	).apply {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			this.datePicker.touchables[1].performClick()
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			this.datePicker.touchables[0].performClick()
		}
	}
}

fun getRandomEmail(): String {

	val builder = StringBuilder()
	builder.append("test_")
	for (i in 0..10) {
		builder.append(Random.nextInt(0, 10))
	}
	builder.append("@gmail.com")

	return builder.toString()

}

fun isPhoneInvalid(phone: String): Boolean = !PhoneNumberUtils.isGlobalPhoneNumber(phone)

fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun dateTimeStringToZonedDateTime(dateTime: String): ZonedDateTime {
	return try {
		ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
	} catch (e: Exception) {
		ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME)
	}
}

fun zonedDateTimeToString(dateTime: ZonedDateTime?): String{
	return if(dateTime == null) "" else dateTime.toString()
}

fun getCurrencyString(amount: String, currency: String): String{
	var currencyString = currency
	if (currency.toLowerCase() == "phl") { // quick fix for Phillippines currency label
		currencyString = "php"
	}
	return "${currencyString.toUpperCase()} $amount"
}