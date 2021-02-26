package reach52.marketplace.community.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.NetworkManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashFragment : androidx.fragment.app.Fragment() {
	private lateinit var disposables: CompositeDisposable

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		disposables = CompositeDisposable()
	}

	private fun displayError() {
		val rxPing = Observable.create<Boolean> {
			it.onNext(NetworkManager.isInternetAvailable())
			it.onComplete()
		}.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({ isOnline ->
					when (isOnline) {
						false -> displayMessage(R.string.you_are_offline)
						else -> displayMessage(R.string.something_error)
					}
				}, {
					displayMessage(R.string.something_error)
				})
		disposables.add(rxPing)
	}

	private fun displayMessage(@StringRes resId: Int) {
		Snackbar.make(view!!, resId, Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.okay, null)
				.show()
	}

	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_splash, container, false).apply {

//			SyncManager.startPullReplication(context, object : SyncManager.PullSyncListener {
//				override fun onError(ex: Throwable) {
//					when (ex) {
//						is SyncManager.SyncOfflineException -> {
//							statusTextView.text = getString(R.string.sync_offline)
//							displayMessage(R.string.you_are_offline)
//							progressTextView.visibility = View.INVISIBLE
//							progressBar.visibility = View.INVISIBLE
//						}
//						else -> {
//							Log.e("taaag", "error: ${ex.message}")
//							statusTextView.text = getString(R.string.sync_error)
//							progressTextView.visibility = View.VISIBLE
//							progressTextView.text = ex.localizedMessage
//							progressBar.visibility = View.INVISIBLE
//						}
//					}
//
//				}
//
//				override fun onStart() {
//					statusTextView.text = getString(R.string.sync_started)
//					progressTextView.visibility = View.INVISIBLE
//					progressBar.visibility = View.VISIBLE
//					progressBar.isIndeterminate = true
//				}
//
//				override fun onProgress(progress: Int, total: Int) {
//					Log.i("taaag", "active: $progress / $total")
//					statusTextView.text = getString(R.string.sync_in_progress)
//					progressTextView.visibility = View.VISIBLE
//					progressBar.progress = progress
//					progressBar.max = total
//					progressTextView.text = String.format("%d / %d", progress, total)
//					progressBar.visibility = View.VISIBLE
//					progressBar.isIndeterminate = false
//				}
//
//				override fun onFinish() {
//					statusTextView.text = getString(R.string.sync_finished)
//					progressTextView.visibility = View.INVISIBLE
//					progressBar.visibility = View.INVISIBLE
//					(activity as MainActivity).gotoResidentsFragment()
//				}
//
//			})

//            val pullEvents = DispergoDatabase.pullEvents.observeOn(AndroidSchedulers.mainThread())

//            disposables.add(pullEvents.subscribe({
//                Log.i("taaag", "splash status: $it")
//                when (it) {
//                    is DispergoDatabase.ReplicationEvent.Error -> {
//
//                        displayError()
//
//                        // NOTE: This disappears too quickly
//                        statusTextView.text = getString(R.string.sync_error)
//                        progressTextView.visibility = View.VISIBLE
//                        progressTextView.text = it.t.localizedMessage
//                        progressBar.visibility = View.INVISIBLE
//                    }
//                    is DispergoDatabase.ReplicationEvent.InProgress -> {
//                        statusTextView.text = getString(R.string.sync_in_progress)
//                        progressTextView.visibility = View.VISIBLE
//                        progressTextView.text = String.format("%d / %d", it.completed, it.total)
//                        progressBar.visibility = View.VISIBLE
//                        progressBar.isIndeterminate = false
//                        progressBar.progress = it.completed
//                        progressBar.max = it.total
//                    }
//                    is DispergoDatabase.ReplicationEvent.Finished -> {
//                        statusTextView.text = getString(R.string.sync_finished)
//                        progressTextView.visibility = View.INVISIBLE
//                        progressBar.visibility = View.INVISIBLE
//                    }
//                    is DispergoDatabase.ReplicationEvent.Started -> {
//                        statusTextView.text = getString(R.string.sync_started)
//                        progressTextView.visibility = View.INVISIBLE
//                        progressBar.visibility = View.VISIBLE
//                        progressBar.isIndeterminate = true
//                    }
//                    is DispergoDatabase.ReplicationEvent.Offline -> {
//                        statusTextView.text = getString(R.string.sync_offline)
//                        displayMessage(R.string.you_are_offline)
//                        progressTextView.visibility = View.INVISIBLE
//                        progressBar.visibility = View.INVISIBLE
//                    }
//                    is DispergoDatabase.ReplicationEvent.UpdatingIndex -> {
//                        statusTextView.text = getString(R.string.updating_index)
//                        progressTextView.visibility = View.INVISIBLE
//                        progressBar.visibility = View.VISIBLE
//                        progressBar.isIndeterminate = true
//                    }
//                }
//            }, {
//                Log.e("SplashFragment", "Replication error", it)
//            }))
		}
	}

	override fun onDestroy() {
		disposables.dispose()
		super.onDestroy()
	}
}
