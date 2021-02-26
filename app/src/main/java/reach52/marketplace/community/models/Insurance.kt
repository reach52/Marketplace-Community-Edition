package reach52.marketplace.community.models

data class Insurance(
        val documentMeta: DocumentMeta,
        val domainResource: InsuranceDomainResource,
        val type: String
)