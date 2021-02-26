package reach52.marketplace.community.auth.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.auth.Auth
import reach52.marketplace.community.auth.viewmodel.LoginActivityViewModel
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.home.view.MainActivity
import reach52.marketplace.community.signup.view.CarouselActivity
import reach52.marketplace.community.signup.view.IDUploadActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

	companion object {
		const val KEY_RE_LOGIN = "re-login"
	}

	private val disposables = CompositeDisposable()

	lateinit var vm: LoginActivityViewModel

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		LanguageUtils.loadLocale(this)
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
	//	toolbar_heading.text = getString(R.string.login)
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

		vm = ViewModelProvider(this)[LoginActivityViewModel::class.java]

		if (BuildConfig.FLAVOR == "live" && BuildConfig.BUILD_TYPE == "release") {
			appVersionText.text = "v${BuildConfig.VERSION_NAME}"
		} else {
			appVersionText.text = "v${BuildConfig.VERSION_NAME} ${BuildConfig.FLAVOR} ${BuildConfig.BUILD_TYPE} - ${BuildConfig.VERSION_CODE}"
		}

		setupContinueButton()

		if (intent.getBooleanExtra(KEY_RE_LOGIN, false)) {
			login_message.text = getString(R.string.login_again)
		}

	}

	private fun setupContinueButton() {
		continue_btn.setOnClickListener {

			val uid = email_edittext.text.toString()
			val pin = pin_input.code!!
			continue_btn.isEnabled = false

			Auth.login(uid, pin, this).subscribe({ result ->

				when (result) {

					Auth.LOGIN_ACCOUNT_SETUP -> {
						// goto account setup page
						Log.i("taaag", "account setup not done ")
						startActivity(Intent(this, AccountSetupActivity::class.java).apply {
//							flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
						})
					}
					Auth.LOGIN_REGISTRATION -> {
						Log.i("taaag", "registration not done")
						startActivity(Intent(this, CarouselActivity::class.java).apply {
							flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
						})
					}
					Auth.LOGIN_PIN_RESET -> {
						// goto PIN setup page
						Log.i("taaag", "going for PIN reset")
						startActivity(Intent(this, PINSetupActivity::class.java))
					}
					Auth.LOGIN_SUCCESS -> {
						// check for photo ID upload
						Log.i("taaag", "login success")
						try {
							idUploadCheck()
						} catch (e: Exception) {
							Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
							continue_btn.isEnabled = true
						}
					}

				}

			}, {

				continue_btn.isEnabled = true

				when (it) {
					is NetworkManager.AccountLockedException -> {
						AlertDialog.Builder(this)
								.setMessage(getString(R.string.account_locked_contact_field_manager))
								.setCancelable(true)
								.setPositiveButton(R.string.ok) { d, _ ->
									d.dismiss()
								}
								.setNegativeButton(R.string.cancel) { d, _ ->
									d.dismiss()
								}
								.create()
								.show()
					}
					else -> {

						val message: String = when (it) {
							is NetworkManager.IncorrectCredsException -> {
								getString(R.string.incorrect_creds)
							}
							is NetworkManager.AccountNotFoundException -> {
								getString(R.string.no_email_found)
							}
							is NetworkManager.AccessDeniedException -> {
								getString(R.string.account_not_allowerd)
							}
							else -> {
								getString(R.string.something_went_wrong)
							}
						}

						Log.e("taaag", "login error: $it")
						Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

					}
				}


			})

		}
	}

	private fun idUploadCheck() {

			val uploaded = Auth.checkIfPhotoIDUploaded(this)
			Log.i("taaag", "photo ID uploaded check: $uploaded")

			if (uploaded) {
				startActivity(Intent(this, MainActivity::class.java).apply {
					flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
				})
			} else {
				startActivity(Intent(this, IDUploadActivity::class.java))
			}

	}

	override fun onDestroy() {
		disposables.clear()

		super.onDestroy()
	}

}
