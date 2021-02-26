package reach52.marketplace.community.models


data class Insured(
        val documentMeta: DocumentMeta,
        val domainResource: InsuredDomainResource,
        val type: String
)