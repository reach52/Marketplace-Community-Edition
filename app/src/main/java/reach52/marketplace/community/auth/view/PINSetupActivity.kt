package reach52.marketplace.community.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.auth.repo.PINRepo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_pin_setup.*

class PINSetupActivity : BaseActivity() {

	val disposables = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pin_setup)

//		pin_input.setEditCodeListener {
//			if (it != null) {
//				done_btn.isEnabled = it.length == pin_input.codeLength
//			} else {
//				done_btn.isEnabled = false
//			}
//		}
		pin_input.setOnKeyListener { v, keyCode, event ->
			if (event.action == KeyEvent.ACTION_DOWN) {
				when (keyCode) {
					KeyEvent.KEYCODE_ENTER -> {
						onDonePress(v)
					}
				}
			}
			false
		}

	}

	fun onDonePress(v: View) {

		val newPIN = pin_input.code
		dispo.add(PINRepo.requestPINReset(this, newPIN).subscribe(
				{
					startActivity(Intent(this, LoginActivity::class.java).apply {
						flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
					})
				},
				{

				}
		))

//		disposables.add(
//				LocalPINManager.savePIN(this, pin_input.code).subscribe(
//						{
//							startActivity(Intent(this, PINEnterActivity::class.java))
//						},
//						{
//							when (it) {
//								is Auth.PINTooShortException -> {
//									Toast.makeText(this, getString(R.string.pin_too_short), Toast.LENGTH_SHORT).show()
//								}
//								is NumberFormatException -> {
//									Toast.makeText(this, getString(R.string.incorrect_pin), Toast.LENGTH_SHORT).show()
//								}
//								else -> {
//									Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
//								}
//							}
//						}
//				)
//		)

	}

	override fun onDestroy() {
		super.onDestroy()
		disposables.dispose()
	}


}