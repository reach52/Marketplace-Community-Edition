package reach52.marketplace.community.persistence

import arrow.core.Option
import arrow.core.getOrElse
import reach52.marketplace.community.models.Insured

class InsuredMapper : Marshaler<Insured>, Unmarshaler<Insured> {

    companion object {
        const val KEY_DOCUMENT_META = "documentMeta"
        const val KEY_DOMAIN_RESOURCE = "domainResource"
        const val KEY_TYPE = "type"
        const val KEY_INSURANCE_OWNER = "insuranceOwner"

        val insuredDocumentMetaMapper = DocumentMetaMapper()
        val insuredDomainResourceMapper = InsuredDomainResourceMapper()
    }

    override fun marshal(t: Insured): Map<String, Any> {
        return mapOf(
                KEY_DOCUMENT_META to insuredDocumentMetaMapper.marshal(t.documentMeta),
                KEY_DOMAIN_RESOURCE to insuredDomainResourceMapper.marshal(t.domainResource),
                KEY_TYPE to t.type
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Insured {
        @Suppress("UNCHECKED_CAST")
        val documentMetaMap = properties[KEY_DOCUMENT_META] as Map<String, Any>

        @Suppress("UNCHECKED_CAST")
        val domainResourceMap = properties[KEY_DOMAIN_RESOURCE] as Map<String, Any>

        return Insured(
                documentMeta = insuredDocumentMetaMapper.unmarshal(documentMetaMap),
                domainResource = insuredDomainResourceMapper.unmarshal(domainResourceMap),
                type = Option.fromNullable(properties[KEY_TYPE]).map { it as String }.getOrElse { "" }
        )
    }
}