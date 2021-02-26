package reach52.marketplace.community.models

import android.content.Context
import arrow.core.Option
import arrow.core.Try
import arrow.core.firstOrNone
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import reach52.marketplace.community.persistence.UserDatabase
import io.reactivex.Observable

class Users(context: Context) {
    private val db = UserDatabase.getInstance(context)
    private val usernameView = UserDatabase.usernameView(context)
    private val mapper = jacksonObjectMapper()

    private fun deserialize(properties: Map<String, Any>) =
            mapper.convertValue(properties, User::class.java)

    fun createOrUpdate(user: User): Observable<User> = Observable.create { o ->
        db.getDocument(user.id).run {
            val mapType = mapper.typeFactory.constructMapType(
                    HashMap::class.java,
                    String::class.java,
                    Any::class.java
            )
            val properties: HashMap<String, Any> = mapper.convertValue(user, mapType)
            update { revision ->
                Try { deserialize(revision.properties) }.fold({
                    // can't deserialize to user, assume new
                    revision.properties = properties
                    o.onNext(user)
                    o.onComplete()
                    true
                }, { savedUser ->
                    if (user != savedUser) {
                        revision.properties = properties
                        o.onNext(user)
                        o.onComplete()
                        true
                    } else {
                        o.onNext(user)
                        o.onComplete()
                        false
                    }
                })
            }
        }
    }

    fun getByUserName(username: String): Observable<Option<User>> = Observable.fromCallable {
        usernameView.createQuery().apply {
            keys = listOf(username)
            limit = 1
        }.run().map { row ->
            deserialize(row.document.properties)
        }.firstOrNone()
    }

}