package reach52.marketplace.community.models

import android.graphics.BitmapFactory
import arrow.core.Option
import arrow.core.getOrElse
import com.couchbase.lite.Database
import reach52.marketplace.community.extensions.toPhotoAttachmentStream
import reach52.marketplace.community.models.insured.Address
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.persistence.InsuredMapper
import org.threeten.bp.Duration
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class Purchases(private val database: Database) {
    companion object {
        private const val CONTENT_TYPE_WEBP = "image/webp"
        private const val KEY_ACKNOWLEDGEMENT_PHOTO = "acknowledgementPhoto"
    }

    private val mapper = InsuredMapper()

    fun residentLogistic(
            acknowledgementPhoto: String,
            plan: InsurancePlan,
            insuranceID: String,
            insurance: InsuranceDomainResource,
            coveredFamilyMembers: MutableList<CoverageInsured>,
            logisticResident: LogisticResident,
            user: Option<User>
    ): String {
        return database.createDocument().run {
            val dateCreated = ZonedDateTime.now(ZoneOffset.UTC)
            val duration = Duration.ofMillis(plan.dateExpiry).toDays()
            val computedCoverageOfInsurance = CoveragePeriod(coverageStart = dateCreated, coverageEnd = dateCreated.plusDays(duration))

            val residentAddress = Address(
                    city = Option.fromNullable(logisticResident.provinceCity),
                    country = Option.fromNullable(logisticResident.country),
                    district = Option.fromNullable(""),
                    line = listOf(logisticResident.address_2),
                    postalCode = Option.fromNullable(logisticResident.postalCode),
                    state = Option.fromNullable("")
            )

            val insurancePlanReference = listOf(PlanReference(
                    title = plan.title,
                    price = plan.price,
                    planOwner = insuranceID,
                    planOwnerName = insurance.name,
                    planReference = plan.identifier,
                    currentStatus = Status(
                            status = "active",
                            statusDate = dateCreated,
                            username = user.map { it.username }.getOrElse { "" },
                            usernameId = user.map { it.username }.getOrElse { "" },
                            userDisplayName = user.map { it.displayName }.getOrElse { "" }
                    ),
                    specificCost = plan.specificCosts,
                    effectiveDate = EffectiveDate(
                            dateStart = dateCreated,
                            dateEnd = dateCreated.plusDays(duration)
                    ),
                    pastStatuses = listOf()
            ))

            val residentInfo = Subject(
                    profileID = logisticResident.residentId,
                    reference = logisticResident.residentId,
                    firstName = logisticResident.firstName,
                    lastName = logisticResident.lastName,
                    middleName = logisticResident.middleName
            )

            val insuranceDomain = InsuredDomainResource(
                    identifier = "None",
                    resourceType = InsuredMapper.KEY_INSURANCE_OWNER,
                    address = residentAddress,
                    dateOfBirth = logisticResident.dateOfBirth,
                    period = computedCoverageOfInsurance,
                    plan = insurancePlanReference,
                    civilStatus = logisticResident.maritalStatus,
                    contact = logisticResident.contact,
                    gender = logisticResident.gender,
                    subject = residentInfo,
                    coverage = coveredFamilyMembers,
                    emailAddress = logisticResident.email
            )

            val documentMeta = DocumentMeta(
                    createdBy = user.map { it.username }.getOrElse { "" },
                    organization = "",
                    dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                    type = InsuredMapper.KEY_INSURANCE_OWNER,
                    source = "dispergo-android"
            )

            val residentInsuranceOwnership = Insured(
                    documentMeta = documentMeta,
                    domainResource = insuranceDomain,
                    type = InsuredMapper.KEY_INSURANCE_OWNER

            )

            putProperties(InsuredMapper().marshal(residentInsuranceOwnership))
            BitmapFactory.decodeFile(acknowledgementPhoto).toPhotoAttachmentStream().let {
                createRevision().apply {
                    setAttachment(KEY_ACKNOWLEDGEMENT_PHOTO, CONTENT_TYPE_WEBP, it)
                    save()
                }
                it.close()
            }
            id
        }
    }

    fun residentAccess(
            acknowledgementPhoto: String,
            plan: InsurancePlan,
            insuranceID: String,
            insurance: InsuranceDomainResource,
            coveredFamilyMembers: MutableList<CoverageInsured>,
            resident: Resident,
            user: Option<User>
    ): String {
        return database.createDocument().run {
            val dateCreated = ZonedDateTime.now(ZoneOffset.UTC)
            val duration = Duration.ofMillis(plan.dateExpiry).toDays()
            val computedCoverageOfInsurance = CoveragePeriod(coverageStart = dateCreated, coverageEnd = dateCreated.plusDays(duration))
            val insurancePlanReference = listOf(PlanReference(
                    title = plan.title,
                    price = plan.price,
                    planOwner = insuranceID,
                    planOwnerName = insurance.name,
                    planReference = plan.identifier,
                    currentStatus = Status(
                            status = "active",
                            statusDate = dateCreated,
                            username = user.map { it.username }.getOrElse { "" },
                            usernameId = user.map { it.username }.getOrElse { "" },
                            userDisplayName = user.map { it.displayName }.getOrElse { "" }
                    ),
                    specificCost = plan.specificCosts,
                    effectiveDate = EffectiveDate(
                            dateStart = dateCreated,
                            dateEnd = dateCreated.plusDays(duration)
                    ),
                    pastStatuses = listOf()
            ))

            val residentInfo = Subject(
                    profileID = resident.id,
                    reference = resident.id,
                    firstName = resident.firstName.getOrElse { "" },
                    lastName = resident.lastName.getOrElse { "" },
                    middleName = resident.middleName.getOrElse { "" }
            )

            val insuranceDomain = InsuredDomainResource(
                    identifier = "None",
                    resourceType = InsuredMapper.KEY_INSURANCE_OWNER,
                    address = resident.address,
                    dateOfBirth = resident.birthDate.atStartOfDay(ZoneOffset.UTC),
                    period = computedCoverageOfInsurance,
                    plan = insurancePlanReference,
                    civilStatus = resident.maritalStatus.getOrElse { "" },
                    contact = resident.contact.getOrElse { "" },
                    gender = resident.gender,
                    subject = residentInfo,
                    coverage = coveredFamilyMembers,
                    emailAddress = resident.email.getOrElse { "" }
            )

            val documentMeta = DocumentMeta(
                    createdBy = user.map { it.username }.getOrElse { "" },
                    organization = "",
                    dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                    type = InsuredMapper.KEY_INSURANCE_OWNER,
                    source = "dispergo-android"
            )

            val residentInsuranceOwnership = Insured(
                    documentMeta = documentMeta,
                    domainResource = insuranceDomain,
                    type = InsuredMapper.KEY_INSURANCE_OWNER

            )

            putProperties(InsuredMapper().marshal(residentInsuranceOwnership))
            BitmapFactory.decodeFile(acknowledgementPhoto).toPhotoAttachmentStream().let {
                createRevision().apply {
                    setAttachment(KEY_ACKNOWLEDGEMENT_PHOTO, CONTENT_TYPE_WEBP, it)
                    save()
                }
                it.close()
            }
            id
        }
    }

    fun get(orderId: String): Insured = mapper.unmarshal(database.getDocument(orderId).properties)
}