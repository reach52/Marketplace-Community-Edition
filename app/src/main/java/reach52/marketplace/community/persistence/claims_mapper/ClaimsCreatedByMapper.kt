package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.CreatedBy
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class ClaimsCreatedByMapper : Unmarshaler<CreatedBy>, Marshaler<CreatedBy> {
    companion object {
        const val KEY_CREATED_BY_ID = "identifier"
        const val KEY_CREATED_BY_USERNAME = "username"
        const val KEY_CREATED_BY_ROLE = "agent"
        const val KEY_CREATED_BY_EMAIL = "email"
    }

    override fun marshal(t: CreatedBy): Map<String, Any> {
        return mapOf(
                KEY_CREATED_BY_ID to t.identifier,
                KEY_CREATED_BY_USERNAME to t.username,
                KEY_CREATED_BY_ROLE to t.role,
                KEY_CREATED_BY_EMAIL to t.email
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CreatedBy {
        val identifier = properties[KEY_CREATED_BY_ID] as String
        val username = properties[KEY_CREATED_BY_USERNAME] as String
        val role = properties[KEY_CREATED_BY_ROLE] as String
        val email = properties[KEY_CREATED_BY_EMAIL] as String

        return CreatedBy(
                identifier = identifier,
                username = username,
                role = role,
                email = email
        )
    }
}