package reach52.marketplace.community.models.logistic_resident

import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZonedDateTime

data class LogisticResident(
        val residentId: String,
        val type: String,
        val source: String,
        val firstName: String,
        val lastName: String,
        val middleName: String,
        val email: String,
        val gender: String,
        val maritalStatus: String,
        val contact: String,
        val dateOfBirth: ZonedDateTime,
        val address_1: String,
        val address_2: String,
        val postalCode: String,
        val provinceCity: String,
        val country: String,
        val createdBy: CreatedBy,
        val updatedBy: List<CreatedBy>
) {
    private val givenName = listOf(firstName, middleName).joinToString(" ")
    val age: Int = Period.between(dateOfBirth.toLocalDate(), LocalDate.now()).years
    val name: String = listOf(lastName, givenName).joinToString()
    val address: String = listOf(address_1, provinceCity, country, postalCode).joinToString(", ")
}