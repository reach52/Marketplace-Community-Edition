package reach52.marketplace.community.models

import reach52.marketplace.community.models.insured.Address

data class Company(
        val identifier: String,
        val name: String,
        val type: String,
        val telecommunication: String,
        val address: List<Address>
)