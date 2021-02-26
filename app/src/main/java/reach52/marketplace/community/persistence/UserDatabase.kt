package reach52.marketplace.community.persistence

import android.content.Context
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import com.couchbase.lite.Database
import com.couchbase.lite.Manager
import com.couchbase.lite.View
import com.couchbase.lite.android.AndroidContext
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import reach52.marketplace.community.models.User

abstract class UserDatabase {
    companion object {
        private const val DB_NAME = "users"
        private const val VIEW_USERNAME = "username"
        private const val VERSION_USERNAME = "1"
        private val mapper = jacksonObjectMapper()

        @Volatile
        private var INSTANCE: Option<Database> = None

        fun getInstance(context: Context): Database = INSTANCE.getOrElse {
            synchronized(this) {
                Manager(AndroidContext(context.applicationContext), Manager.DEFAULT_OPTIONS)
                        .getDatabase(DB_NAME)
                        .apply {
                            getView(VIEW_USERNAME)?.apply {
                                setMap({ document, emitter ->
                                    val user = mapper.convertValue(document, User::class.java)
                                    emitter.emit(user.username, null)
                                }, VERSION_USERNAME)
                            }
                            INSTANCE = Some(this)
                        }
            }
        }

        fun usernameView(context: Context): View = getInstance(context).getView(VIEW_USERNAME)
    }
}