package reach52.marketplace.community.persistence

import android.content.Context
import arrow.core.Option
import com.couchbase.lite.Database
import com.couchbase.lite.Manager
import com.couchbase.lite.View
import com.couchbase.lite.android.AndroidContext
import com.couchbase.lite.auth.AuthenticatorFactory
import com.couchbase.lite.replicator.Replication
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.extensions.utils.CountryCode
import reach52.marketplace.community.extensions.utils.CountryManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.net.URL

abstract class ResidentDatabase {
    sealed class ReplicationEvent {
        data class Error(val t: Throwable) : ReplicationEvent()
        data class InProgress(val completed: Int, val total: Int) : ReplicationEvent()
        object Finished : ReplicationEvent()
        object Offline : ReplicationEvent()
        object Started : ReplicationEvent()
        object UpdatingIndex : ReplicationEvent()
    }

    //Not used anymore
    companion object {
        private val DB_NAME = when (BuildConfig.FLAVOR) {
            "live" -> when (CountryManager.getCountryCode()) {
                CountryCode.IND -> ""
                else -> ""
            }
            "demo" -> ""
            else -> ""
        }

        private val mapper = ResidentMapper()

//        Not used anymore
        private val REPLICATION_URL = when (BuildConfig.FLAVOR) {
            "demo" -> URL("")
            "live" -> when (CountryManager.getCountryCode()) {
                CountryCode.IND -> URL("")
                else -> URL("")
            }
            else -> URL("")
        }

//        Not used anymore
        private val AUTHENTICATOR = when (BuildConfig.FLAVOR) {
            "demo" -> AuthenticatorFactory.createBasicAuthenticator("", "")
            "live" -> when (CountryManager.getCountryCode()) {
                CountryCode.IND -> AuthenticatorFactory.createBasicAuthenticator("", "")
                else -> null
            }
            else -> null
        }

        @Volatile
        private var DB_INSTANCE: Database? = null

        private fun getInstance(context: Context): Database = DB_INSTANCE ?: synchronized(this){
            var databaseName = DB_NAME

//            Auth.currentSession(context).map { session ->
//                if(session.user.roles.contains("self_signup_mam")){
//                    databaseName += "_" + session.user.id
//                }
//            }

            DB_INSTANCE ?: Manager(AndroidContext(context.applicationContext), Manager.DEFAULT_OPTIONS)
                    .getDatabase(databaseName)
                    .apply {
                DB_INSTANCE = this
            }
        }

        private const val VERSION_RESIDENTS = "5"
        private const val VIEW_RESIDENTS = "residents"

        @Volatile
        private var RESIDENTS : View? = null

        fun residentsView(context: Context): View = RESIDENTS ?: synchronized(this) {
            RESIDENTS ?: getInstance(context).getView(VIEW_RESIDENTS).apply {
                setMap({ document, emitter ->
                    Utils.getModel(ResidentMapper.TYPE_RESIDENT, mapper, document).filter {
                            it.name.isNotBlank()
                        }.map {
                            emitter.emit(it.name, null)
                        }
                }, VERSION_RESIDENTS)
                RESIDENTS = this
            }
        }

        @Volatile
        private var PULL_INSTANCE: Replication? = null

        private fun getPullReplication(context: Context): Replication = PULL_INSTANCE
                ?: synchronized(this) {
                    PULL_INSTANCE
                            ?: getInstance(context).createPullReplication(REPLICATION_URL).apply {
                                authenticator = AUTHENTICATOR
                                PULL_INSTANCE = this
                            }
                }

        private val pullChangeEvents: PublishSubject<ReplicationEvent> = PublishSubject.create()

        val pullEvents: Observable<ReplicationEvent> = pullChangeEvents

        fun startReplication(context: Context) = getPullReplication(context).run {
            addChangeListener {
                when (it.status!!) {
                    Replication.ReplicationStatus.REPLICATION_ACTIVE -> {
                        Option.fromNullable(it.error).fold({
                            pullChangeEvents.onNext(
                                    ReplicationEvent.InProgress(
                                            it.completedChangeCount,
                                            it.changeCount
                                    )
                            )
                        }, { e ->
                            pullChangeEvents.onNext(ReplicationEvent.Error(e))
                        })
                    }
                    Replication.ReplicationStatus.REPLICATION_OFFLINE -> {
                        pullChangeEvents.onNext(ReplicationEvent.Offline)
                    }
                    Replication.ReplicationStatus.REPLICATION_STOPPED -> {
                        Option.fromNullable(it.error).fold({
                            pullChangeEvents.onNext(ReplicationEvent.UpdatingIndex)
                            residentsView(context).updateIndex()
                            pullChangeEvents.onNext(ReplicationEvent.Finished)
                        }, { e ->
                            pullChangeEvents.onNext(ReplicationEvent.Error(e))
                        })
                    }
                    Replication.ReplicationStatus.REPLICATION_IDLE -> {
                        // NOOP
                    }
                }
            }
            start()
            pullChangeEvents.onNext(ReplicationEvent.Started)
        }

        fun stopReplication() {
            PULL_INSTANCE?.stop()
        }

        fun resetResidentDB() {
            synchronized(this){
                DB_INSTANCE = null
                RESIDENTS = null
            }
        }
    }
}