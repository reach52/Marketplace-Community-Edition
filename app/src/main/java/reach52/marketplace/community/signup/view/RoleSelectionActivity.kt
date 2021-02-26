package reach52.marketplace.community.signup.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.extensions.utils.SharedPrefs
import kotlinx.android.synthetic.main.toolbar.*


class RoleSelectionActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_role_selection)

		help_btn.visibility = View.VISIBLE
		help_btn.setOnClickListener {
			startActivity(Intent(this, FAQActivity::class.java))
		}

//		normal_user_btn.setOnClickListener {
//			onRoleSelect(AppUser.ROLE_CONSUMER)
//		}
//
//		community_manager_btn.setOnClickListener {
//			onRoleSelect(AppUser.ROLE_MAM)
//		}

		back_btn.setOnClickListener{
			super.onBackPressed()
		}

	}

	private fun onRoleSelect(role: String) {

		// save user selection
		SharedPrefs.write(SharedPrefs.KEY_ROLE, role)

		startActivity(Intent(this, RegistrationActivity::class.java))
	}

}