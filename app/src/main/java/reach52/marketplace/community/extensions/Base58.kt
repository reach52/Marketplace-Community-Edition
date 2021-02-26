@file:Suppress("SpellCheckingInspection")

package reach52.marketplace.community.extensions

import java.nio.ByteBuffer
import java.util.*

@ExperimentalUnsignedTypes
fun UUID.base58String() = this.toByteArray().encodeToBase58String()

private fun UUID.toByteArray() = ByteBuffer.allocate(16).let {
    it.putLong(this.mostSignificantBits)
    it.putLong(this.leastSignificantBits)
}.array()

// https://github.com/komputing/KEthereum/blob/master/base58/src/main/kotlin/org/kethereum/encodings/Base58.kt

private const val ENCODED_ZERO = '1'
private const val alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"


/**
 * Encodes the bytes as a base58 string (no checksum is appended).
 *
 * @return the base58-encoded string
 */
@ExperimentalUnsignedTypes
private fun ByteArray.encodeToBase58String(): String {

    val input = copyOf(size) // since we modify it in-place
    if (input.isEmpty()) {
        return ""
    }
    // Count leading zeros.
    var zeros = 0
    while (zeros < input.size && input[zeros].toInt() == 0) {
        ++zeros
    }
    // Convert base-256 digits to base-58 digits (plus conversion to ASCII characters)
    val encoded = CharArray(input.size * 2) // upper bound
    var outputStart = encoded.size
    var inputStart = zeros
    while (inputStart < input.size) {
        encoded[--outputStart] =
                alphabet[divMod(input, inputStart.toUInt(), 256.toUInt(), 58.toUInt()).toInt()]
        if (input[inputStart].toInt() == 0) {
            ++inputStart // optimization - skip leading zeros
        }
    }
    // Preserve exactly as many leading encoded zeros in output as there were leading zeros in data.
    while (outputStart < encoded.size && encoded[outputStart] == ENCODED_ZERO) {
        ++outputStart
    }
    while (--zeros >= 0) {
        encoded[--outputStart] = ENCODED_ZERO
    }
    // Return encoded string (including encoded leading zeros).
    return String(encoded, outputStart, encoded.size - outputStart)
}

// private val alphabetIndices by lazy {
//     IntArray(128) { alphabet.indexOf(it.toChar()) }
// }

/**
 * Decodes the base58 string into a [ByteArray]
 *
 * @return the decoded data bytes
 * @throws NumberFormatException if the string is not a valid base58 string
 */
// @Throws(NumberFormatException::class)
// private fun String.decodeBase58(): ByteArray {
//     if (isEmpty()) {
//         return ByteArray(0)
//     }
//     // Convert the base58-encoded ASCII chars to a base58 byte sequence (base58 digits).
//     val input58 = ByteArray(length)
//     for (i in 0 until length) {
//         val c = this[i]
//         val digit = if (c.toInt() < 128) alphabetIndices[c.toInt()] else -1
//         if (digit < 0) {
//             throw NumberFormatException("Illegal character $c at position $i")
//         }
//         input58[i] = digit.toByte()
//     }
//     // Count leading zeros.
//     var zeros = 0
//     while (zeros < input58.size && input58[zeros].toInt() == 0) {
//         ++zeros
//     }
//     // Convert base-58 digits to base-256 digits.
//     val decoded = ByteArray(length)
//     var outputStart = decoded.size
//     var inputStart = zeros
//     while (inputStart < input58.size) {
//         decoded[--outputStart] = divMod(input58, inputStart.toUInt(), 58.toUInt(), 256.toUInt()).toByte()
//         if (input58[inputStart].toInt() == 0) {
//             ++inputStart // optimization - skip leading zeros
//         }
//     }
//     // Ignore extra leading zeroes that were added during the calculation.
//     while (outputStart < decoded.size && decoded[outputStart].toInt() == 0) {
//         ++outputStart
//     }
//     // Return decoded data (including original number of leading zeros).
//     return decoded.copyOfRange(outputStart - zeros, decoded.size)
// }


@ExperimentalUnsignedTypes
private fun divMod(number: ByteArray, firstDigit: UInt, base: UInt, divisor: UInt): UInt {
    // this is just long division which accounts for the base of the input digits
    var remainder = 0.toUInt()
    for (i in firstDigit until number.size.toUInt()) {
        val digit = number[i.toInt()].toUByte()
        val temp = remainder * base + digit
        number[i.toInt()] = (temp / divisor).toByte()
        remainder = temp % divisor
    }
    return remainder
}
