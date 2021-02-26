package reach52.marketplace.community.extensions.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import com.couchbase.lite.replicator.RemoteRequest
import com.google.firebase.crashlytics.FirebaseCrashlytics
import reach52.marketplace.community.DispergoApp
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


object NetworkManager {

	const val POST = 1
	const val PUT = 2

	private fun hasActiveInternetConnection(): Boolean {
		return try {
			val urlc = (URL("http://clients3.google.com/generate_204").openConnection()) as HttpURLConnection
			urlc.setRequestProperty("User-Agent", "Android")
			urlc.setRequestProperty("Connection", "close")
			urlc.connectTimeout = 1500
			urlc.connect()
			println(urlc.responseCode)
			println(urlc.contentLength)
			return urlc.responseCode == 204 && urlc.contentLength == 0
		} catch (e: java.lang.Exception) {
			println(e)
			false
		}
	}


	private val client = OkHttpClient.Builder()
			.connectTimeout(120, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.build()

	fun makeGETRequest(url: String, params: String = "", context: Context = DispergoApp.get()) = Single.create<SimpleResponse> {

		if (!isInternetAvailable()) {
			it.onError(NoInternetConnectionException())
			return@create
		}

		val req = Request.Builder()
				.url(if (params.isNullOrEmpty()) url else "$url/$params")
				.build()
		logRequest(req, null)
		client.newCall(req).enqueue(object : Callback {
			override fun onFailure(call: Call, e: IOException) {
				it.onError(e)
			}

			override fun onResponse(call: Call, response: Response) {

				handleResponse(response, it)

			}
		})

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun makePOSTRequest(url: String, jsonBody: JSONObject, headers: Map<String, String>? = null, context: Context = DispergoApp.get()) =
			makePOSTorPUTRequest(POST, url, jsonBody, headers, context)

	fun makePUTRequest(url: String, jsonBody: JSONObject, headers: Map<String, String>? = null, context: Context = DispergoApp.get()) =
			makePOSTorPUTRequest(PUT, url, jsonBody, headers, context)

	private fun makePOSTorPUTRequest(method: Int, url: String, jsonBody: JSONObject, headers: Map<String, String>? = null, context: Context = DispergoApp.get()) = Single.create<SimpleResponse> {

		if (!isInternetAvailable()) {
			it.onError(NoInternetConnectionException())
			return@create
		}

		val body = RequestBody.create(RemoteRequest.JSON, jsonBody.toString())
		val rb = Request.Builder()
				.url(url)
		val reqBuilder = if (method == PUT) {
			rb.put(body)
		} else {
			rb.post(body)
		}


		if (headers != null) {
			for (entry in headers.entries) {
				reqBuilder.addHeader(entry.key, entry.value)
			}
		}


		val req = reqBuilder.build()

		logRequest(req, body)
		client.newCall(req).enqueue(object : Callback {
			override fun onFailure(call: Call, e: IOException) {
				it.onError(e)
			}

			override fun onResponse(call: Call, response: Response) {

				handleResponse(response, it)

			}

		})


	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


	private fun handleResponse(response: Response, it: SingleEmitter<SimpleResponse>) {
		val string = response.body()?.string()
		logResponse(string)

		when (response.code()) {
			200, 201, 202 -> {
				val sr = SimpleResponse(
						response.code(),
						response.message(),
						response.isSuccessful,
						JSONObject(string)
				)
				it.onSuccess(sr)
			}
			401 -> {
				it.onError(IncorrectCredsException())
			}
			403 -> {
				it.onError(AccessDeniedException())
			}
			404 -> {
				it.onError(AccountNotFoundException())
			}
			423 -> {
				it.onError(AccountLockedException())
			}
			409 -> {
				it.onError(UsernameTakenException())
			}
			406 -> {
				it.onError(InvalidRegistrationDataException())
			}
			else -> {
				it.onError(Exception("${response.code()}: ${response.message()}"))
			}
		}


		response.body()?.close()
	}

	private fun logResponse(responseString: String?) {
		Log.d("taaag", "response: $responseString")
	}

	private fun logRequest(request: Request, payload: Any? = null) {
		val payLd = when (payload) {
			is Map<*, *> -> {
				payload.toString()
			}
			else -> {
				payload.toString()
			}
		}
		Log.d("taaag", "request: ${request.method()} ${request.url()}\nbody: $payLd")
	}

	fun makeMultipartRequest(
			method: Int = POST,
			url: String,
			stringData: Map<String, String?>? = null,
			files: Map<String, File?>? = null,
			headers: Map<String, String>? = null,
			context: Context = DispergoApp.get()) = Single.create<SimpleResponse> {

		if (!isInternetAvailable()) {
			it.onError(NoInternetConnectionException())
			return@create
		}

		val multipartRequestBuilder = MultipartBody.Builder()
				.setType(MultipartBody.FORM)


		if (files != null) {
			for (file in files) {

				val f = file.value as File

				if (f != null) {
					Log.d("taaag", "${file.key} size: ${f.length()}")
					val ext = MimeTypeMap.getFileExtensionFromUrl(file.toString())
					val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
					multipartRequestBuilder.addFormDataPart(
							file.key,
							f.absolutePath,
							RequestBody.create(MediaType.parse(mime), file.value)
					)
				}
			}
		}

		if (stringData != null) {

			for (string in stringData) {
				if (string.value != null) {
					multipartRequestBuilder.addFormDataPart(string.key, string.value)
				}
			}

		}

		val requestBody = multipartRequestBuilder.build()


		val rb = Request.Builder()
				.url(url)


		val requestBuilder =
				if (method == POST)
					rb.post(requestBody)
				else
					rb.put(requestBody)

		if (headers != null) {
			for (entry in headers.entries) {
				requestBuilder.addHeader(entry.key, entry.value)
			}
		}

		val req = requestBuilder.build()
		logRequest(req, files)
		client.newCall(req).enqueue(object : Callback {
			override fun onFailure(call: Call, e: IOException) {
				it.onError(e)
				FirebaseCrashlytics.getInstance().recordException(e)
			}

			override fun onResponse(call: Call, response: Response) {

				handleResponse(response, it)

			}

		})

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	data class SimpleResponse(
			val code: Int,
			val message: String,
			val isSuccessfull: Boolean,
			val json: JSONObject
	)

	fun isInternetAvailable(): Boolean {

		val cm = DispergoApp.get().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

		if (cm != null) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				val nc = cm.getNetworkCapabilities(cm.activeNetwork)
				return nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
			} else {
				return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
			}

		} else {
			return hasActiveInternetConnection()
		}

	}

	class NoInternetConnectionException : Exception("Not connected to internet")
	class AccessDeniedException : Exception("Access Denied")
	class IncorrectCredsException : Exception()
	class AccountNotFoundException : Exception()
	class AccountLockedException : Exception()

	class UsernameTakenException : Exception()
	class InvalidRegistrationDataException : Exception()

}