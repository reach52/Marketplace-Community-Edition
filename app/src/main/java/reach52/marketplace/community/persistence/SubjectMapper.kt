package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.Subject

class SubjectMapper : Marshaler<Subject>, Unmarshaler<Subject> {
    companion object {
        const val KEY_SUBJECT_PROFILE_ID = "profileID"
        const val KEY_SUBJECT_REFERENCE = "reference"
        const val KEY_SUBJECT_FIRST_NAME = "firstName"
        const val KEY_SUBJECT_LAST_NAME = "lastName"
        const val KEY_SUBJECT_MIDDLE_NAME = "middleName"
    }

    override fun marshal(t: Subject): Map<String, Any> {
        return mapOf(
                KEY_SUBJECT_PROFILE_ID to t.profileID,
                KEY_SUBJECT_REFERENCE to t.reference,
                KEY_SUBJECT_FIRST_NAME to t.firstName,
                KEY_SUBJECT_MIDDLE_NAME to t.middleName,
                KEY_SUBJECT_LAST_NAME to t.lastName
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Subject {
        val profileID = properties[KEY_SUBJECT_PROFILE_ID] as String
        val reference = properties[KEY_SUBJECT_REFERENCE] as String
        val firstName = properties[KEY_SUBJECT_FIRST_NAME] as String
        val lastName = properties[KEY_SUBJECT_LAST_NAME] as String
        val middleName = properties[KEY_SUBJECT_MIDDLE_NAME] as String

        return Subject(
                profileID = profileID,
                reference = reference,
                firstName = firstName,
                lastName = lastName,
                middleName = middleName
        )
    }
}