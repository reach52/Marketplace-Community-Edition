package reach52.marketplace.community.auth.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {

	fun encrypt(data: String, key: String): String {
		val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
		val iv = ByteArray(32)
		val charArray = key.toCharArray()
		for (i in charArray.indices) {
			iv[i] = charArray[i].toByte()
		}
		val ivParameterSpec = IvParameterSpec(iv)

		val cipher = Cipher.getInstance("AES/GCM/NoPadding")
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

		val encryptedValue = cipher.doFinal(data.toByteArray())
		return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
	}

	fun decrypt(data: String, key: String): String {
		val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
		val iv = ByteArray(32)
		val charArray = key.toCharArray()
		for (i in charArray.indices) {
			iv[i] = charArray[i].toByte()
		}
		val ivParameterSpec = IvParameterSpec(iv)

		val cipher = Cipher.getInstance("AES/GCM/NoPadding")
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

		val decryptedByteValue = cipher.doFinal(Base64.decode(data, Base64.DEFAULT))
		return String(decryptedByteValue)
	}

}