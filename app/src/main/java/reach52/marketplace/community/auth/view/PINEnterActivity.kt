package reach52.marketplace.community.auth.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.auth.Auth
import reach52.marketplace.community.auth.repo.PINRepo
import reach52.marketplace.community.home.view.MainActivity
import reach52.marketplace.community.signup.view.IDUploadActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_pin_enter.*

class PINEnterActivity : BaseActivity() {

	val disposables = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pin_enter)

		pin_input.requestFocus()
		pin_input.setOnKeyListener { v, keyCode, event ->
			if (event.action == KeyEvent.ACTION_DOWN) {
				when (keyCode) {
					KeyEvent.KEYCODE_ENTER -> {
						onEnterPress(v)
					}
				}
			}
			false
		}


		forgot_pin.setOnClickListener {

			AlertDialog.Builder(this)
					.setMessage(getString(R.string.ask_help_from_pin_manager_for_forgot_password))
					.setCancelable(true)
					.setPositiveButton(R.string.ok) { d, _ ->
						d.dismiss()
						Auth.logout(this)
						gotoLoginActivity()
					}
					.setNegativeButton(R.string.cancel) { d, _ ->
						d.dismiss()
					}
					.create()
					.show()


		}

	}

	fun onEnterPress(v: View) {

		try {
			val success = PINRepo.verifyPIN(this, pin_input.code)
			if (success) {
				hideKeyboard(v) {
					idUploadCheck()
				}

			} else {
				Toast.makeText(this, getString(R.string.incorrect_pin), Toast.LENGTH_SHORT).show()
			}
		} catch (e: Exception) {
			when (e) {
				is PINRepo.InvalidPINException -> { // go to login if no PIN found
					gotoLoginActivity()
				}
				is PINRepo.PINTooShortException -> {
					Toast.makeText(this, getString(R.string.incomplete_pin), Toast.LENGTH_SHORT).show()
				}
				is NumberFormatException -> {
					Toast.makeText(this, getString(R.string.incorrect_pin), Toast.LENGTH_SHORT).show()
				}
				else -> {
					Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
				}
			}
		}

	}

	private fun gotoLoginActivity() {

		startActivity(Intent(this@PINEnterActivity, LoginActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		})

	}

	private fun idUploadCheck() {
		try {
			val uploaded = Auth.checkIfPhotoIDUploaded(this)
			Log.i("taaag", "photo ID uploaded check: $uploaded")

			if (uploaded) {
				startActivity(Intent(this, MainActivity::class.java).apply {
					flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
				})
			} else {
				startActivity(Intent(this, IDUploadActivity::class.java))
			}
		} catch (e: java.lang.Exception) {

			Toast.makeText(this, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
		}

	}

	override fun onDestroy() {
		super.onDestroy()
		disposables.dispose()
	}

}