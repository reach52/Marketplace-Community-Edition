package reach52.marketplace.community

import android.app.Application

class DispergoApp : Application() {

	companion object {
		var debounce = false

		lateinit var app: DispergoApp
		fun get() = app
	}

	override fun onCreate() {
		super.onCreate()
		app = this

	}

}