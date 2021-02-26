package reach52.marketplace.community.activities

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.extensions.utils.MyContextWrapper
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

	val dispo = CompositeDisposable()

	override fun attachBaseContext(newBase: Context?) {
		val language = LanguageUtils.getSavedLanguage()
		super.attachBaseContext(MyContextWrapper.wrap(newBase!!, language))
	}

	protected fun hideStatusbar() {
		this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
	}

	protected fun hideKeyboard(v: View, onHide: () -> Unit = {}) {
		Handler().postDelayed({
			val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputMethodManager.hideSoftInputFromWindow(v.rootView.windowToken, 0)
			onHide()
		}, 1)
	}

	override fun onDestroy() {
		dispo.dispose()
		super.onDestroy()
	}

	protected fun gotoFragment(fragment: Fragment, emptyStack: Boolean = false, addToBackStack: Boolean = false) {

		if (fragment.isAdded)
			return

		if (emptyStack) {
			supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
		}

		val trans = supportFragmentManager
				.beginTransaction()

		if (addToBackStack) {
			trans.add(R.id.fragment_container, fragment)
			trans.addToBackStack(null)
		} else {
			trans.replace(R.id.fragment_container, fragment)
		}
		trans.commit()

	}


}