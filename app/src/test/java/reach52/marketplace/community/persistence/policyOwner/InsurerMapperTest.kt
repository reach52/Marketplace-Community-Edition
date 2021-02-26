package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.insurance.Benefit
import reach52.marketplace.community.models.policy_owner.*
import reach52.marketplace.community.persistence.policyOwner_mapper.*
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class InsurerMapperTest {

    private val mapper  = InsurerMapper()
    private val insurerFixture = Insurer(
            insurerName = "Malayan",
            reference = "MLN",
            certificateNumber = "123",
            unit = "KHR",
            plan = listOf(Plan(
                    identifier = "7Y3OhYkpCx",
                    tier = "Health & Personal Accident Plan",
                    rate = 98568.00,
                    category = "adult",
                    benefits = listOf(Benefit(
                            category = "Hospitalization without major surgery (Public health facilities)",
                            amount = 0.00,
                            identifier = "H_W_O_MS_PHF"
                        )
                    )
                )
            ),
            attachments =  listOf("category1"),
            qualification =  Qualification(
                    data =  mapOf("a" to "b"),
                    reviewedDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    denialReason = "reason",
                    reviewedBy = "someone",
                    isAccepted = true
            )
    )

    private val insurerMap = mapOf(
            InsurerMapper.KEY_INSURER_NAME to "Malayan",
            InsurerMapper.KEY_REFERENCE_INSURER to "MLN",
            InsurerMapper.KEY_CERTIFICATE_NUMBER to "123",
            InsurerMapper.KEY_UNIT to "KHR",
            InsurerMapper.KEY_PLAN to listOf(mapOf(
                    PlanMapper.KEY_IDENTIFIER to "7Y3OhYkpCx",
                    PlanMapper.KEY_TIER to "Health & Personal Accident Plan",
                    PlanMapper.KEY_RATE to 98568.00,
                    PlanMapper.KEY_CATEGORY to "adult",
                    PlanMapper.KEY_BENEFITS to listOf(mapOf(
                            BenefitMapper.KEY_CATEGORY to "Hospitalization without major surgery (Public health facilities)",
                            BenefitMapper.KEY_AMOUNT to 0.0,
                            BenefitMapper.KEY_IDENTIFIER to "H_W_O_MS_PHF"
                    ))
            )),
            InsurerMapper.KEY_ATTACHMENT_INSURER to listOf("category1"),
            InsurerMapper.KEY_QUALIFICATION to mapOf(
                    QualificationMapper.KEY_DATA to mapOf("a" to "b"),
                    QualificationMapper.KEY_REVIEWED_DATE to "1970-01-01T00:00:00Z",
                    QualificationMapper.KEY_DENIAL_REASON to "reason",
                    QualificationMapper.KEY_REVIEWED_BY to "someone",
                    QualificationMapper.KEY_IS_ACCEPTED to true
            )
    )

    @Test
    fun marshal() {
        Assert.assertEquals(insurerMap, mapper.marshal(insurerFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(insurerFixture, mapper.unmarshal(insurerMap))
    }

}