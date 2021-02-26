package reach52.marketplace.community.persistence

import android.util.Log
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.Try
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import java.util.*

abstract class Utils {
    companion object {

        fun <T> getModel(type: String, unmarshaler: Unmarshaler<T>, document: Map<String, Any>
        ): Option<T> =
                Option.fromNullable(document["type"]).flatMap {
                    Log.d("ryann", "document type = ${document["type"]}")
                    if (it.toString() == type) {
                        Try { unmarshaler.unmarshal(document) }.fold({
                            Log.e("taaaag", it.message)
                            it.printStackTrace()
                            None
                        }, { model ->
                            Some(model)
                        })
                    } else {
                        None
                    }
                }


        fun getDateFromZonedTime(dateTimeString: ZonedDateTime): String {
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTime(Date())
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            return sdf.format(dateTimeString)

        }
    }
}