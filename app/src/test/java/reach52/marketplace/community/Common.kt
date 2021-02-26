package reach52.marketplace.community

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.mockito.Mockito

fun <T> When(methodCall: T) = Mockito.`when`(methodCall)

fun commonSetup(context: Context?){
	RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

	if(context != null) {
		val cm = Mockito.mock(ConnectivityManager::class.java)
		val nc = Mockito.mock(NetworkCapabilities::class.java)
		val netInfo = Mockito.mock(NetworkInfo::class.java)

		When(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?).thenReturn(cm)
		When(nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(true)
		When(cm!!.activeNetworkInfo).thenReturn(netInfo)
		When(cm.activeNetworkInfo.isConnected).thenReturn(true)
	}

}

fun goOffline(context: Context) {
	val cm = Mockito.mock(ConnectivityManager::class.java)
	val nc = Mockito.mock(NetworkCapabilities::class.java)
	val netInfo = Mockito.mock(NetworkInfo::class.java)
	When(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?).thenReturn(cm)
	When(nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(false)
	When(cm!!.activeNetworkInfo).thenReturn(netInfo)
	When(cm.activeNetworkInfo.isConnected).thenReturn(false)
}