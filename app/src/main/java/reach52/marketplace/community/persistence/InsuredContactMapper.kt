package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.InsuredContact

class InsuredContactMapper : Marshaler<InsuredContact>, Unmarshaler<InsuredContact> {
    companion object {
        const val KEY_CODE = "code"
        const val KEY_NUMBER = "number"
    }

    override fun marshal(t: InsuredContact): Map<String, Any> {
        return mapOf(
                KEY_CODE to t.code,
                KEY_NUMBER to t.number
        )
    }

    override fun unmarshal(properties: Map<String, Any>): InsuredContact {
        val code = properties[KEY_CODE] as String
        val number = properties[KEY_NUMBER] as String

        return InsuredContact(
                code = code,
                number = number
        )
    }
}