package reach52.marketplace.community.utils

import reach52.marketplace.community.extensions.utils.NetworkManager
import io.reactivex.Observable
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkManagerTest {
    @Test
    fun hasActiveInternetConnection() {
        Observable.create<Boolean> { emitter ->
            emitter.onNext(NetworkManager.hasActiveInternetConnection())
            emitter.onComplete()
        }.subscribe({
            assertTrue(it)
        }, {
            assertTrue(false)
        })
    }
}