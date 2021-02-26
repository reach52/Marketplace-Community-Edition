package reach52.marketplace.community.models

import arrow.core.Option
import arrow.core.Some
import reach52.marketplace.community.extensions.catOptions
import reach52.marketplace.community.models.insured.Address
import org.threeten.bp.LocalDate
import org.threeten.bp.Period

data class Resident(
        val address: Address,
        val birthDate: LocalDate,
        val firstName: Option<String>,
        val gender: String,
        val id: String,
        val lastName: Option<String>,
        val maritalStatus: Option<String>,
        val middleName: Option<String>,
        val contact: Option<String>,
        val email: Option<String>
) {
    private val givenName = listOf(firstName, middleName).catOptions().joinToString(" ")

    val age: Int = Period.between(birthDate, LocalDate.now()).years
    val name: String = listOf(lastName, Some(givenName)).catOptions().joinToString()
}
