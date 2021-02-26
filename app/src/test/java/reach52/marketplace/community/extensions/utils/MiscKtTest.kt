package reach52.marketplace.community.extensions.utils

import org.junit.Test

import org.junit.Assert.*

class MiscKtTest {

	@Test
	fun `calculate age from incorrect date format`() {

		try {
			val age = calculateAgeFromDOB("50/4/1995")
			assertEquals(25, age)
		} catch (e: NumberFormatException) {
			assertTrue(true)
		}

	}


	@Test
	fun `calculate age from correct date format`() {

		val age = calculateAgeFromDOB("1995-4-25")
		assertEquals("age: $age",25, age)

	}


}