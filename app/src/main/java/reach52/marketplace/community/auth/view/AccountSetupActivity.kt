package reach52.marketplace.community.auth.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.auth.Auth
import reach52.marketplace.community.auth.repo.PINRepo
import reach52.marketplace.community.databinding.ActivityAccountSetupBinding
import reach52.marketplace.community.extensions.utils.NetworkManager
import kotlinx.android.synthetic.main.toolbar.*

class AccountSetupActivity : BaseActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val b = DataBindingUtil.setContentView<ActivityAccountSetupBinding>(this, R.layout.activity_account_setup)
		toolbar_heading.text = getString(R.string.account_setup)

		b.continueBtn.setOnClickListener {
			val username = b.usernameEdittext.text.toString()
			val pin = b.pinInput.code!!
			Auth.accountSetup(this, username, pin).subscribe(
					{
						startActivity(Intent(this, LoginActivity::class.java).apply {
							putExtra(LoginActivity.KEY_RE_LOGIN, true)
							flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
						})
					},
					{
						when (it) {
							is PINRepo.InvalidPINException -> {
								Toast.makeText(this, getString(R.string.invalid_pin), Toast.LENGTH_SHORT).show()
							}
							is PINRepo.PINTooShortException -> {
								Toast.makeText(this, R.string.pin_too_short, Toast.LENGTH_SHORT).show()
							}
							is NetworkManager.UsernameTakenException -> {
								Toast.makeText(this, getString(R.string.username_taken_error), Toast.LENGTH_SHORT).show()
							}
							is Auth.InvalidUsernameException -> {
								Toast.makeText(this, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show()
							}
							is Auth.UsernameTooShortException -> {
								Toast.makeText(this, getString(R.string.username_too_short), Toast.LENGTH_SHORT).show()
							}
						}
					}
			)
		}

	}
}