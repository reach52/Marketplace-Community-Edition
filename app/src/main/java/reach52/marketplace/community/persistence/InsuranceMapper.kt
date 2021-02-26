package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.Insurance

class InsuranceMapper : Marshaler<Insurance>, Unmarshaler<Insurance> {

    companion object {
        const val KEY_DOCUMENT_META = "documentMeta"
        const val KEY_DOMAIN_RESOURCE = "domainResource"
        const val KEY_TYPE = "type"
        const val KEY_TYPE_INSURANCE_PLAN = "insurancePlan"

        private val documentMetaMapper = InsuranceDocumentMetaMapper()
        private val insuranceDomainResource = InsuranceDomainResourceMapper()
    }

    override fun marshal(t: Insurance): Map<String, Any> {
        return mapOf(
                KEY_DOCUMENT_META to documentMetaMapper.marshal(t.documentMeta),
                KEY_DOMAIN_RESOURCE to insuranceDomainResource.marshal(t.domainResource),
                KEY_TYPE to t.type

        )
    }

    override fun unmarshal(properties: Map<String, Any>): Insurance {
        @Suppress("UNCHECKED_CAST")
        val documentMeta = documentMetaMapper.unmarshal(properties[KEY_DOCUMENT_META] as Map<String, Any>)

        @Suppress("UNCHECKED_CAST")
        val domainResource = insuranceDomainResource.unmarshal(properties[KEY_DOMAIN_RESOURCE] as Map<String, Any>)
        val type = properties[KEY_TYPE] as String

        return Insurance(
                documentMeta = documentMeta,
                domainResource = domainResource,
                type = type
        )
    }

}