package reach52.marketplace.community.persistence.insurance

import arrow.core.Some
import reach52.marketplace.community.models.*
import reach52.marketplace.community.models.insured.Address
import reach52.marketplace.community.persistence.*
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class InsuranceMapperTest {

    private val mapper = InsuranceMapper()

    private val insuranceFixture = Insurance(
            documentMeta = DocumentMeta(
                    dateCreated = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    createdBy = "Malayan",
                    organization = "sample org",
                    source = "malayan source",
                    type = "insurancePlan"
            ),
            domainResource = InsuranceDomainResource(
                    identifier = "id123",
                    name = "AWH",
                    code = "code123",
                    dateCreated = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    ownedBy = Company(
                            identifier = "id123",
                            name = "Malayan",
                            type = "health",
                            telecommunication = "tel123",
                            address = listOf(Address(
                                    city = Some("Manila City"),
                                    country = Some("PH"),
                                    district = Some("District 1"),
                                    line = listOf("123"),
                                    postalCode = Some("1601"),
                                    state = Some("Region 1"))
                            )
                    ),
                    insurancePlan = listOf(InsurancePlan(
                            identifier = "id123",
                            title = "Medication",
                            code = "code123",
                            dateExpiry = 31536000000,
                            price = 100.00,
                            details = "Some details",
                            specificCosts = listOf(SpecificCost(
                                    identifier = "id123",
                                    category = "sample cat",
                                    cost = 100.00
                            ))
                    )),
                    coverage = listOf(CoverageInsurance(
                            benefit = listOf(BenefitInsurance(
                                    planReference = "some plan",
                                    type = "typeA",
                                    requirement = listOf(Requirement(
                                            value = "some value",
                                            identifier = "id123"
                                    ))
                            ))
                    ))

            ),
            type = "insurancePlan"
    )

    private val mapFixture = mapOf(

            "documentMeta" to mapOf(
                    InsuranceDocumentMetaMapper.KEY_DATE_CREATED to "1970-01-01T00:00:00Z",
                    InsuranceDocumentMetaMapper.KEY_CREATED_BY to "Malayan",
                    InsuranceDocumentMetaMapper.KEY_ORGANIZATION to "sample org",
                    InsuranceDocumentMetaMapper.KEY_SOURCE to "malayan source",
                    InsuranceDocumentMetaMapper.KEY_TYPE to "insurancePlan"
            ),

            "domainResource" to mapOf(InsuranceDomainResourceMapper.KEY_IDENTIFIER to "id123",
                    InsuranceDomainResourceMapper.KEY_NAME to "AWH",
                    InsuranceDomainResourceMapper.KEY_CODE to "code123",
                    InsuranceDomainResourceMapper.KEY_DATE_CREATED to "1970-01-01T00:00:00Z",
                    InsuranceDomainResourceMapper.KEY_OWNED_BY to mapOf(
                            CompanyInsuranceMapper.KEY_IDENTIFIER to "id123",
                            CompanyInsuranceMapper.KEY_NAME to "Malayan",
                            CompanyInsuranceMapper.KEY_TYPE to "health",
                            CompanyInsuranceMapper.KEY_TELECOMMUNICATION to "tel123",
                            CompanyInsuranceMapper.KEY_ADDRESS to listOf(
                                    mapOf(
                                            AddressMapper.KEY_CITY to "Manila City",
                                            AddressMapper.KEY_COUNTRY to "PH",
                                            AddressMapper.KEY_DISTRICT to "District 1",
                                            AddressMapper.KEY_LINE to listOf("123"),
                                            AddressMapper.KEY_POSTAL_CODE to "1601",
                                            AddressMapper.KEY_STATE to "Region 1"
                                    )
                            )),
                    InsuranceDomainResourceMapper.KEY_PLAN to listOf(
                            mapOf(
                                    InsurancePlanMapper.KEY_IDENTIFIER to "id123",
                                    InsurancePlanMapper.KEY_TITLE to "Medication",
                                    InsurancePlanMapper.KEY_CODE to "code123",
                                    InsurancePlanMapper.KEY_PRICE to 100.00,
                                    InsurancePlanMapper.KEY_DATE_EXPIRY to 31536000000,
                                    InsurancePlanMapper.KEY_DETAILS to "Some details",
                                    InsurancePlanMapper.KEY_SPECIFIC_COST to listOf(
                                            mapOf(
                                                    SpecificCostInsuranceMapper.KEY_IDENTIFIER to "id123",
                                                    SpecificCostInsuranceMapper.KEY_CATEGORY to "sample cat",
                                                    SpecificCostInsuranceMapper.KEY_COST to 100.00
                                            )
                                    )
                            )
                    ),

                    InsuranceDomainResourceMapper.KEY_COVERAGE to listOf(
                            mapOf(
                                    CoverageInsuranceMapper.KEY_BENEFITS to listOf(
                                            mapOf(
                                                    BenefitsInsuranceMapper.KEY_TYPE to "typeA",
                                                    BenefitsInsuranceMapper.KEY_PLAN_REFERENCE to "some plan",
                                                    BenefitsInsuranceMapper.KEY_REQUIREMENT to listOf(
                                                            mapOf(
                                                                    RequirementsInsuranceMapper.KEY_IDENTIFIER to "id123",
                                                                    RequirementsInsuranceMapper.KEY_VALUE to "some value"
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            ),

            "type" to "insurancePlan"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(insuranceFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(insuranceFixture, mapper.unmarshal(mapFixture))
    }

}