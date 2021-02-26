package reach52.marketplace.community.extensions.utilsimport android.content.Contextimport android.telephony.TelephonyManagerimport android.util.Logimport reach52.marketplace.community.BuildConfigimport reach52.marketplace.community.DispergoAppimport io.reactivex.Singleclass CountryManager {	/**	 * This method tries to get country code from telephone Manager	 * If not found, makes an API call to check the country	 * If offline, checks if country already stored	 * If still no country found, throws CountryNotFoundException	 */	fun detectCountry(context: Context) = Single.create<String> {		val iso2Code = if (BuildConfig.BUILD_TYPE == "debug") {			"kh"		} else {			getCountryFromSIM(context)		}		if (iso2Code != null) {			val iso3Code = getISO3from2(iso2Code)			if (iso3Code == null) {				it.onError(CountryNotSupportedException())				return@create			} else {				Log.d("taaag", "sim country code = $iso3Code")				it.onSuccess(iso3Code)				return@create			}		}		NetworkManager.makeGETRequest("http://ip-api.com/json", context = context).subscribe(				{ response ->					val c = response.json.getString("countryCode")					val cc = getISO3from2(c)					Log.i("taaag", "api country code = $cc")					if (cc == null) {						it.onError(CountryNotSupportedException())					} else {						it.onSuccess(cc)					}				},				{ err ->					if (err is NetworkManager.NoInternetConnectionException) {						val savedCode = readSavedCountryCode(context)						if (savedCode == null) {							it.onError(CountryNotFoundException())						} else {							it.onSuccess(savedCode)						}					} else {						it.onError(err)					}				}		)	}	fun saveCountryCode(context: Context, code: String) {		SharedPrefs.write(SharedPrefs.KEY_COUNTRY, code, context = context)	}	private fun readSavedCountryCode(context: Context): String? = SharedPrefs.readString(SharedPrefs.KEY_COUNTRY, null, context)	private fun getCountryFromSIM(context: Context): String? {		val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager		if (tm == null) {			return null		}		if (tm.simState != TelephonyManager.SIM_STATE_READY) {			return null		}		return tm.simCountryIso	}	private fun getISO3from2(iso2: String) = when (iso2.toLowerCase()) {		"in" -> CountryCode.IND		"ph" -> CountryCode.PHL		"kh" -> CountryCode.KHM		"sg" -> if (BuildConfig.FLAVOR == "uat") CountryCode.SGP else null		else -> null	}	companion object {		val code by lazy {			CountryManager().readSavedCountryCode(DispergoApp.app)!!		}		fun getCountryCode(): String {			return code		}		fun getCountryCodeOf(country: String) = when (country) {			"Cambodia" -> CountryCode.KHM			"Philippines" -> CountryCode.PHL			"India" -> CountryCode.IND			"Singapore" -> CountryCode.SGP			else -> null		}		fun getCountry(countryCode: String) = when (countryCode) {			CountryCode.KHM -> "Cambodia"			CountryCode.PHL -> "Philippines"			CountryCode.IND -> "India"			CountryCode.SGP -> "Singapore"			else -> null		}	}	fun getCountryPhoneCode(countryCode: String): String {		return when (countryCode) {			CountryCode.IND -> "+91"			CountryCode.PHL -> "+63"			CountryCode.KHM -> "+855"			else -> ""		}	}	class CountryNotSupportedException : Exception()	class CountryNotFoundException : Exception()}