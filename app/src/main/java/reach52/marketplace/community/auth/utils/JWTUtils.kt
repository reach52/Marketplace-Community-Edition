package reach52.marketplace.community.auth.utils

import com.auth0.android.jwt.JWT

object JWTUtils {

	fun decode(token: String, claim: String): Map<*, *> {
		val decodedJWT = JWT(token)
		return decodedJWT.claims[claim]!!.asObject(Map::class.java)!!
	}

}