package reach52.marketplace.community.signup.view

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.auth.view.LoginActivity
import kotlinx.android.synthetic.main.activity_intro_video.*

class IntroVideoActivity : BaseActivity() {

	var position: Int = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_intro_video)

		val orientation = resources.configuration.orientation
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			hideStatusbar()
		}

		val mediaController = MediaController(this)
		mediaController.setAnchorView(videoView)
		val video: Uri = Uri.parse("android.resource://$packageName/${R.raw.intro}")
		videoView.setMediaController(mediaController)
		videoView.setVideoURI(video)

		if (savedInstanceState != null) {

			val position = savedInstanceState.getInt("position")
			Log.i("taaag", "reading position: $position")
			videoView.seekTo(position)

		}
		videoView.start()


	}

	fun onFinishClick(v: View) {
		startActivity(Intent(this, LoginActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			putExtra(LoginActivity.KEY_RE_LOGIN, true)
		})
	}

	override fun onPause() {
		super.onPause()
		position = videoView.currentPosition
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)

		outState.putInt("position", position)
		Log.i("taaag", "saving position: $position")

	}

}