package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.EffectiveDate
import reach52.marketplace.community.models.PlanReference
import reach52.marketplace.community.models.SpecificCost
import reach52.marketplace.community.models.Status
import reach52.marketplace.community.persistence.EffectiveDateMapper
import reach52.marketplace.community.persistence.InsuredStatusMapper
import reach52.marketplace.community.persistence.PlanReferenceMapper
import reach52.marketplace.community.persistence.SpecificCostMapper
import org.junit.Assert.assertEquals
import org.junit.Test

import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class InsurancePlanReferenceMapperTest {

    private val planMapperModelFixture = PlanReference(
            title = "title1",
            price = 12.00,
            planReference = "plan reference",
            planOwner = "plan owner",
            planOwnerName = "plan owner name",
            currentStatus = Status(
                    status = "status",
                    statusDate = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    username = "username",
                    usernameId = "usernameID",
                    userDisplayName = "user display name"
            ),
            pastStatuses = listOf(
                    Status(
                            status = "status",
                            statusDate = ZonedDateTime.parse("2019-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                            username = "username",
                            usernameId = "username ID",
                            userDisplayName = "user display name"
                    )
            ),
            specificCost = listOf(
                    SpecificCost(
                            identifier = "_id",
                            category = "category",
                            cost = 12.00
                    )
            ),
            effectiveDate = EffectiveDate(
                    dateStart = ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                    dateEnd = ZonedDateTime.of(2020, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC)
            )

    )

    private val planMapperMapFixture = mapOf(
            PlanReferenceMapper.KEY_PLAN_TITLE to "title1",
            PlanReferenceMapper.KEY_PLAN_PRICE to 12.00,
            PlanReferenceMapper.KEY_PLAN_REFERENCE to "plan reference",
            PlanReferenceMapper.KEY_PLAN_OWNER to "plan owner",
            PlanReferenceMapper.KEY_PLAN_OWNER_NAME to "plan owner name",
            PlanReferenceMapper.KEY_PLAN_CURRENT_STATUS to mapOf(
                    InsuredStatusMapper.KEY_STATUS to "status",
                    InsuredStatusMapper.KEY_STATUS_DATE to "2020-07-05T00:00Z",
                    InsuredStatusMapper.KEY_USERNAME to "username",
                    InsuredStatusMapper.KEY_USERNAME_ID to "usernameID",
                    InsuredStatusMapper.KEY_USER_DISPLAY_NAME to "user display name"
            ),
            PlanReferenceMapper.KEY_PLAN_PAST_STATUSES to listOf<Map<String, Any>>(
                    mapOf(
                            InsuredStatusMapper.KEY_STATUS to "status",
                            InsuredStatusMapper.KEY_STATUS_DATE to "2019-07-05T00:00Z",
                            InsuredStatusMapper.KEY_USERNAME to "username",
                            InsuredStatusMapper.KEY_USERNAME_ID to "username ID",
                            InsuredStatusMapper.KEY_USER_DISPLAY_NAME to "user display name"
                    )
            ),
            PlanReferenceMapper.KEY_PLAN_SPECIFIC_COST to listOf(
                    mapOf(
                            SpecificCostMapper.KEY_IDENTIFIER to "_id",
                            SpecificCostMapper.KEY_CATEGORY to "category",
                            SpecificCostMapper.KEY_COST to 12.00
                    )
            ),
            PlanReferenceMapper.KEY_PLAN_EFFECTIVE_DATE to mapOf(
                    EffectiveDateMapper.KEY_EFFECTIVE_DATE_START to "2019-07-05T00:00:00Z",
                    EffectiveDateMapper.KEY_EFFECTIVE_DATE_END to "2020-07-05T00:00:00Z"
            )
    )

    @Test
    fun unmarshal() {
        assertEquals(planMapperModelFixture, PlanReferenceMapper().unmarshal(planMapperMapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(planMapperMapFixture, PlanReferenceMapper().marshal(planMapperModelFixture))
    }
}