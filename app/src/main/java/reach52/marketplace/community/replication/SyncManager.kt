package reach52.marketplace.community.replication

import android.content.Context
import android.util.Log
import com.couchbase.lite.auth.AuthenticatorFactory
import com.couchbase.lite.replicator.Replication
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.net.URL


class SyncManager(context: Context) {

	private val disposables = CompositeDisposable()

	init {
		createPullReplication(context)
		createPushReplication(context)
	}

	companion object {
		private lateinit var pull: Replication
		private lateinit var push: Replication
		private const val flavour = BuildConfig.FLAVOR
		private val url = when (flavour) {

		// Database connection

			// Live database URL connection
			"live" -> URL("")

			// Demo database URL connection
			"demo" -> URL("")

			// UAT database URL connection
			else -> URL("")

		}

		//Please add your authenticator here
		//Please add your authenticator here
		private val auth = when (flavour) {
			// Live Database Authenticator
			"live" -> AuthenticatorFactory.createBasicAuthenticator("", "")

			// Demo Database Authenticator
			"demo" -> AuthenticatorFactory.createBasicAuthenticator("", "")

			// UAT Database Authenticator
			else -> AuthenticatorFactory.createBasicAuthenticator("", "")
		}
		private val changeListeners = ArrayList<Replication.ChangeListener>()
		private fun createPullReplication(context: Context) {
			if (!::pull.isInitialized) {

				val user = LocalUserRepo.getUser(context)

				pull = DispergoDatabase.getInstance(context).createPullReplication(url)
				pull.authenticator = auth
				val channels = ArrayList<String>()
				channels += user.id
				user.catalogTags.forEach {
					channels += it.trim()
				}
				pull.channels = channels
				Log.d("taaag", "channels: ${pull.channels}")
				Log.d("taaag", "pull sync url: $url")
			}
		}

		private fun createPushReplication(context: Context) {

			if (!::push.isInitialized) {

				push = DispergoDatabase.getInstance(context).createPushReplication(url)
				push.authenticator = auth
				push.isContinuous = true

			}

		}

	}

	fun startPullReplication() {
		pull.start()
	}

	fun startPushReplication() {
		push.start()
	}

	fun addPullListener(listener: SyncListener?) {
		disposables.add(
				Observable.create<Replication.ChangeEvent> {
					val listener = Replication.ChangeListener { event ->
						Log.v("taaag", "event: ${event.status}, ${event.transition?.source} -> ${event.transition?.destination}, (${event.completedChangeCount} / ${event.changeCount}) err: ${event.error},")
						it.onNext(event)
					}
					changeListeners += listener
					pull.addChangeListener(listener)
				}
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe { event ->
							if (event.error != null) {
								listener?.onError(event.error)
							} else {

								when (event.status) {
									Replication.ReplicationStatus.REPLICATION_OFFLINE -> {
										listener?.onError(SyncOfflineException())
									}
									Replication.ReplicationStatus.REPLICATION_ACTIVE -> {
										listener?.onProgress(
												event.completedChangeCount,
												event.changeCount
										)
									}
									Replication.ReplicationStatus.REPLICATION_STOPPED -> {
										if (event.error != null) {
											listener?.onError(event.error)
										} else {
											listener?.onFinish()
										}
									}
									else -> {
									}
								}

							}
						}

		)
	}

	fun addPushListener(listener: SyncListener?) {
		disposables.add(
				Observable.create<Replication.ChangeEvent> {
					val listener = Replication.ChangeListener { event ->
						Log.v("taaag", "push event: ${event.status}, ${event.transition?.source} -> ${event.transition?.destination}, (${event.completedChangeCount} / ${event.changeCount}) err: ${event.error},")
						it.onNext(event)
					}
					changeListeners += listener
					push.addChangeListener(listener)
				}
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe { event ->
							if (event.error != null) {
								listener?.onError(event.error)
							} else {

								when (event.status) {
									Replication.ReplicationStatus.REPLICATION_OFFLINE -> {
										listener?.onError(SyncOfflineException())
									}
									Replication.ReplicationStatus.REPLICATION_ACTIVE -> {
										listener?.onProgress(
												event.completedChangeCount,
												event.changeCount
										)
									}
									Replication.ReplicationStatus.REPLICATION_STOPPED -> {
										if (event.error != null) {
											listener?.onError(event.error)
										} else {
											listener?.onFinish()
										}
									}
									else -> {
									}
								}

							}
						}

		)
	}

	fun stopPullReplication() {

		pull.stop()

	}

	fun stopPushReplication() {
		push.stop()
	}

	fun dispose() {
		changeListeners.forEach {
			pull.removeChangeListener(it)
			push.removeChangeListener(it)
		}
		changeListeners.clear()
		disposables.dispose()
	}

	class SyncOfflineException : Exception()

	interface SyncListener {

		fun onError(ex: Throwable)

		fun onProgress(progress: Int, total: Int)

		fun onFinish()

	}

}