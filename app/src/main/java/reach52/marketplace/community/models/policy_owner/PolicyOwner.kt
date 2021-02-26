package reach52.marketplace.community.models.policy_owner

import reach52.marketplace.community.models.Dependent
import org.threeten.bp.ZonedDateTime

data class PolicyOwner(
        var type: String,
        var policyOwnerID: String,
        var policyOwnerFullName: String,
        var address: String,
        var email: String,
        var contact: String,
        var civilStatus: String,
        var gender: String,
        var dateOfBirth: ZonedDateTime,
        var insured: Insured,
        var beneficiaries: List<Beneficiaries>,
        var insurer: Insurer,
        var status: String,
        var application: ZonedDateTime,
        var effectiveDate: ZonedDateTime,
        var expiry: ZonedDateTime,
        var lastUpdated: ZonedDateTime,
        var createdBy: String,
        var pastStatuses: List<Status>,
        var updatedBy: UpdatedBy,

        var dependents: List<Dependent>?

)