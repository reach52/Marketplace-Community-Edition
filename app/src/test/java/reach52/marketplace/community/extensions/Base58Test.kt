@file:Suppress("SpellCheckingInspection")

package reach52.marketplace.community.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

@ExperimentalUnsignedTypes
class Base58Test {
    @Test
    fun base58String() {
        val expected = "8eGNFKR3HBmD1pZHLecV4J"
        val actual = UUID.fromString("3de3f5be-e73f-4f24-bbc8-4b4c4d109557").base58String()
        assertEquals(expected, actual)
    }
}
