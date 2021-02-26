package reach52.marketplace.community.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.auth.repo.PINRepo
import reach52.marketplace.community.auth.view.LoginActivity
import reach52.marketplace.community.auth.view.PINEnterActivity
import reach52.marketplace.community.extensions.utils.*
import reach52.marketplace.community.extensions.utils.UpdateManager.HIGH_PRIORITY
import reach52.marketplace.community.extensions.utils.UpdateManager.LOW_PRIORITY
import reach52.marketplace.community.extensions.utils.UpdateManager.MED_PRIORITY
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		if (BuildConfig.FLAVOR == "live" && BuildConfig.BUILD_TYPE == "release") {
			version_text.text = "v${BuildConfig.VERSION_NAME}"
		} else {
			version_text.text = "v${BuildConfig.VERSION_NAME} ${BuildConfig.FLAVOR} ${BuildConfig.BUILD_TYPE} - ${BuildConfig.VERSION_CODE}"
		}

		Log.i("taaag", "v${BuildConfig.VERSION_NAME} ${BuildConfig.FLAVOR} ${BuildConfig.BUILD_TYPE} - ${BuildConfig.VERSION_CODE}")

		LanguageUtils.loadLocale(this)

	}

	override fun onStart() {
		super.onStart()

		val updateTask = getForceAppUpdateTask()
		val countrySetupTask = getCountrySetupTask()
		val delay = Completable.timer(2, TimeUnit.SECONDS)

		dispo.add(Completable.mergeArray(
				updateTask,
				countrySetupTask,
				delay
		).subscribe({

			// check if language is set
			val lang = SharedPrefs.readString(SharedPrefs.KEY_LANGUAGE, null)
			Log.i("taaag", "language: $lang")

			if (lang == null) {

				// go to language selection page
				startActivity(Intent(this, LanguageSelectionActivity::class.java))

			} else {

				if (LocalUserRepo.userDataExistsLocally()) {

					if (PINRepo.isPINResetSet()) {
						gotoLoginActivity()
						return@subscribe
					}

					if (PINRepo.getPIN(this) == null) {
						gotoLoginActivity()
						return@subscribe
					}

					gotoPINEnter()

				} else {
					gotoLoginActivity()
				}

			}


		}, {

			when (it) {
				is CountryManager.CountryNotSupportedException -> {
					splash_message.text = getString(R.string.country_not_supported)
				}
				else -> {
					Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
				}
			}

		}))

	}

	private fun gotoLoginActivity() {
		startActivity(Intent(this, LoginActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		})
	}

	private fun gotoPINEnter() {
		startActivity(Intent(this, PINEnterActivity::class.java))
	}

	private fun getCountrySetupTask(): Completable {

		val countryManager = CountryManager()

		return countryManager.detectCountry(this).flatMapCompletable { countryCode ->

			countryManager.saveCountryCode(this, countryCode)
			Log.i("taaag", "country: $countryCode")
			Completable.complete()

		}

	}

	private fun getForceAppUpdateTask() = Completable.create {

		val dialog = AlertDialog.Builder(this)
				.setCancelable(false)
				.create()

		UpdateManager.getForceAppUpdateTask(this)
				.subscribe(
						{ update ->
							val priority = update.priority
							val newVersionCode = update.code
							val currentVersionCode = BuildConfig.VERSION_CODE


							if (newVersionCode > currentVersionCode) {
								Log.i("taaag", "update status: current=$currentVersionCode | new=$newVersionCode | priority=$priority")
								when (priority) {
									HIGH_PRIORITY -> {
										// goto to play store
										dialog.setMessage(getString(R.string.please_update_the_app_to_continue))
										dialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.goto_playstore)) { dialog, _ ->
											dialog.dismiss()
											gotoPlayStore()
											finish()
										}
										dialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { dialog, _ ->
											dialog.dismiss()
											finish()
										}
										dialog.show()

									}
									MED_PRIORITY -> {
										// notify user
										dialog.setMessage(getString(R.string.do_you_want_to_install_the_update))
										dialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.goto_playstore)) { dialog, _ ->
											gotoPlayStore()
											finish()
										}
										dialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.later)) { dialog, _ ->
											dialog.dismiss()
											it.onComplete()
										}
										dialog.show()

									}
									LOW_PRIORITY -> {
										it.onComplete()
									}
								}
							} else {
								Log.i("taaag", "no update available")
								it.onComplete()
							}

						},
						{ err ->
							if (err is NetworkManager.NoInternetConnectionException) {
								it.onComplete()
								return@subscribe
							}

							Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()

						}
				)


	}

//	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//		super.onActivityResult(requestCode, resultCode, data)
//
//		if(requestCode == 111){
//
//			val ans = data!!.getSerializableExtra(FormActivity.KEY_ANSWERS)
//			Log.i("taaag", "answers")
//
//		}
//
//	}

	private fun gotoPlayStore() {
		val appPackageName = packageName // getPackageName() from Context or Activity object
		try {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
		} catch (anfe: ActivityNotFoundException) {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
		}
	}

}