package reach52.marketplace.community.models.policy_owner

import reach52.marketplace.community.models.insurance.Benefit

data class Plan (
        val identifier : String,
        val tier : String,
        val rate : Double,
        val category : String,
        val benefits : List<Benefit>
)
