package reach52.marketplace.community.app

import android.app.Application
import android.content.Context
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import reach52.marketplace.community.data.LoginService
import reach52.marketplace.community.data.PaymentGatewayService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DispergoApp : Application() {

    private val TAG = "DispergoApp"

    companion object{

        private var sID: DispergoApp? = null
        var context: Context? = null

        fun getInstance(): DispergoApp? {
            return sID
        }
        fun getAppContext(): Context? {
            return sID?.applicationContext
        }

        fun create2(): LoginService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    GsonConverterFactory.create()
                )

                .baseUrl("your open API")
                .client(buildHttpClient())
                .build()

            return retrofit.create(LoginService::class.java)

        }
        fun create3(): PaymentGatewayService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl("your UAT payment gateway API")
                .client(buildHttpClient())
                .build()

            return retrofit.create(PaymentGatewayService::class.java)

        }

        private fun buildHttpClient(): OkHttpClient? {
            val httpClient = OkHttpClient.Builder()
            val dispatcher = Dispatcher()
            dispatcher.maxRequestsPerHost = 10
            httpClient.dispatcher(dispatcher)
            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            httpClient.writeTimeout(60, TimeUnit.SECONDS) // connect timeout
            httpClient.readTimeout(60, TimeUnit.SECONDS) // socket timeout
            httpClient.addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header("Content-Type", "header1")
                    .header("Client-Service", "header2")
                    .header("Auth-Key", "App key")
                    .build()
                chain.proceed(newRequest)
            }
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
            return httpClient.build()
        }
    }
}