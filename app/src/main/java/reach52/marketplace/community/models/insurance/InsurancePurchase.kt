package reach52.marketplace.community.models.insurance

import android.graphics.BitmapFactory
import arrow.core.Option
import arrow.core.getOrElse
import com.couchbase.lite.Database
import reach52.marketplace.community.extensions.toPhotoAttachmentStream
import reach52.marketplace.community.models.Dependent
import reach52.marketplace.community.models.User
import reach52.marketplace.community.models.policy_owner.*
import reach52.marketplace.community.models.policy_owner.Qualification
import reach52.marketplace.community.models.policy_owner.UpdatedBy
import reach52.marketplace.community.persistence.policyOwner_mapper.PolicyOwnerMapper
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class InsurancePurchase(private val database: Database) {
    companion object {
        private const val CONTENT_TYPE_WEBP = "image/webp"
        private const val KEY_ACKNOWLEDGEMENT_PHOTO = "acknowledgementPhoto"
    }

    private val mapper = PolicyOwnerMapper()

    fun addPurchase(
            acknowledgementPhoto: String,
            beneficiaries: MutableList<Beneficiaries>,
            insured: Insured,
            policyOwnerID : String,
            policyOwnerFullName : String,
            policyOwnerAddress : String,
            policyOwnerEmail : String,
            policyOwnerContact : String,
            policyOwnerCivilStatus : String,
            policyOwnerGender : String,
            policyOwnerDateOfBirth : ZonedDateTime,
            insuranceId: String,
            formulary: Formulary,
            certificateNumber: String,
            unit: String,
            insurerName: String,
            user: Option<User>,
            answers: Map<String,Any>,
            status : String,
            dependents: List<Dependent>?
    ) {
        database.createDocument().run {

            val insurer = Insurer(
                    insurerName = insurerName,
                    reference = insuranceId,
                    unit = unit,
                    certificateNumber = certificateNumber,
                    plan = listOf(Plan(
                            identifier = formulary.identifier,
                            tier = formulary.tier,
                            rate = formulary.rate,
                            category = formulary.category,
                            benefits = formulary.benefits
                    )),
                    attachments = listOf("ID"),
                    qualification = Qualification(
                            data = answers,
                            reviewedDate = ZonedDateTime.now(),
                            denialReason = "",
                            isAccepted = true,
                            reviewedBy = ""
                    )
            )

            val expiry :ZonedDateTime = ZonedDateTime.of(LocalDate.now().plusYears(1).atStartOfDay(), ZoneOffset.UTC)
            val policyOwner = PolicyOwner(
                    type = PolicyOwnerMapper.TYPE_POLICY_OWNER,
                    policyOwnerID = policyOwnerID,
                    policyOwnerFullName = policyOwnerFullName,
                    address = policyOwnerAddress,
                    email = policyOwnerEmail,
                    contact = policyOwnerContact,
                    civilStatus = policyOwnerCivilStatus,
                    gender = policyOwnerGender,
                    dateOfBirth = policyOwnerDateOfBirth,
                    insured = insured,
                    beneficiaries = beneficiaries,
                    insurer = insurer,
                    status = status,
                    application = ZonedDateTime.now(),
                    effectiveDate = ZonedDateTime.now(),
                    expiry = expiry,
                    lastUpdated = ZonedDateTime.now(),
                    createdBy = user.map { it.username }.getOrElse { "" },
                    pastStatuses = listOf(),
                    updatedBy = UpdatedBy(
                            reference = user.map { it.username }.getOrElse { "" },
                            display = user.map { it.displayName }.getOrElse { "" }
                    ),
                    dependents = dependents

            )

            putProperties(PolicyOwnerMapper().marshal(policyOwner))
            BitmapFactory.decodeFile(acknowledgementPhoto).toPhotoAttachmentStream().let {
                createRevision().apply {
                    setAttachment(KEY_ACKNOWLEDGEMENT_PHOTO, CONTENT_TYPE_WEBP, it)
                    save()
                }
                it.close()
            }
        }
    }

    fun addPolicyInfo(
            insuranceId: String,
            insurerName: String,
            policyOwnerID : String,
            policyOwnerFullName : String,
            policyOwnerAddress : String,
            policyOwnerEmail : String,
            policyOwnerContact : String,
            policyOwnerCivilStatus : String,
            policyOwnerGender : String,
            policyOwnerDateOfBirth : ZonedDateTime,
            formulary: Formulary,
            unit: String,
            user: Option<User>,
            answers: Map<String,Any>
    ){
        database.createDocument().run {

            val localDate: LocalDate = LocalDate.of(1, 1, 1)
            val localTime: LocalTime =
                    LocalTime.of(0, 0, 0, 0)

            val insurer = Insurer(
                    insurerName = insurerName,
                    reference = insuranceId,
                    unit = unit,
                    certificateNumber = "",
                    plan = listOf(Plan(
                            identifier = formulary.identifier,
                            tier = formulary.tier,
                            rate = formulary.rate,
                            category = formulary.category,
                            benefits = formulary.benefits
                    )),
                    attachments = listOf("ID"),
                    qualification = Qualification(
                            data = answers,
                            reviewedDate = ZonedDateTime.now(),
                            denialReason = "",
                            isAccepted = false,
                            reviewedBy = ""
                    )
            )

            val insured = Insured(
                    reference = "",
                    display = "",
                    address = "",
                    email = "",
                    contact = "",
                    civilStatus = "",
                    gender = "",
                    dateOfBirth = ZonedDateTime.now(),
                    relationship = ""
            )

            val beneficiaries = listOf(Beneficiaries(
                    reference = "",
                    display = "",
                    relationship = ""
            ))

            val policyOwner = PolicyOwner(
                    type = PolicyOwnerMapper.TYPE_POLICY_OWNER,
                    policyOwnerID = policyOwnerID,
                    policyOwnerFullName = policyOwnerFullName,
                    address = policyOwnerAddress,
                    email = policyOwnerEmail,
                    contact = policyOwnerContact,
                    civilStatus = policyOwnerCivilStatus,
                    gender = policyOwnerGender,
                    dateOfBirth = policyOwnerDateOfBirth,
                    insured = insured,
                    beneficiaries = beneficiaries,
                    insurer = insurer,
                    status = "pending questionnaire",
                    application = ZonedDateTime.now(),
                    effectiveDate = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC),
                    expiry = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC),
                    createdBy = user.map { it.username }.getOrElse { "" },
                    lastUpdated = ZonedDateTime.now(),
                    pastStatuses = listOf(),
                    updatedBy = UpdatedBy(
                            reference = user.map { it.username }.getOrElse { "" },
                            display = user.map { it.displayName }.getOrElse { "" }
                    ),
                    dependents = listOf()
            )

            putProperties(PolicyOwnerMapper().marshal(policyOwner))
        }
    }

    fun updatePolicy(
            insured: Insured,
            policyOwnerId: String,
            beneficiaries: MutableList<Beneficiaries>,
            acknowledgementPhoto: String,
            status: String
    ){
        database.getDocument(policyOwnerId).update{
            val policyOwner = mapper.unmarshal(it.properties)

            val updatedPolicy = policyOwner.copy(
                    beneficiaries = beneficiaries,
                    insured = insured,
                    status = status,
                    lastUpdated = ZonedDateTime.now()
            )

            it.userProperties = mapper.marshal(updatedPolicy)

            BitmapFactory.decodeFile(acknowledgementPhoto).toPhotoAttachmentStream().let { inputStream ->
                it.setAttachment(KEY_ACKNOWLEDGEMENT_PHOTO, CONTENT_TYPE_WEBP, inputStream)
            }

            true
        }
    }

    fun get(policyOwnerDocumentID:  String): PolicyOwner = mapper.unmarshal(database.getDocument(policyOwnerDocumentID).properties)
}
