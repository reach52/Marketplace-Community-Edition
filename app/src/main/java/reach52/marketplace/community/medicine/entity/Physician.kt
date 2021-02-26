package reach52.marketplace.community.medicine.entity

data class Physician (
        val id: String,
        val licenseNumber: String,
        val physicianName: Name,
        val medicalSpecialty: String
){
    data class Name (
            val givenName: String,
            val familyName: String,
            val middleName: String
    )

    override fun toString(): String {
        return (physicianName.familyName).plus(" ").plus(physicianName.givenName)
    }

    fun physicianFullName(): String {
        return "${physicianName.familyName},  ${physicianName.givenName}"
    }
}