package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.policy_owner.Qualification
import reach52.marketplace.community.persistence.policyOwner_mapper.QualificationMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class QualificationMapperTest {

    private val mapper = QualificationMapper()
    private val qualificationFixture = Qualification(
            data =  mapOf("a" to "b"),
            reviewedDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            denialReason = "reason",
            reviewedBy = "someone",
            isAccepted = true
    )

            private val mapFixture = mapOf(
                    QualificationMapper.KEY_DATA to mapOf("a" to "b"),
                    QualificationMapper.KEY_REVIEWED_DATE to "1970-01-01T00:00:00Z",
                    QualificationMapper.KEY_DENIAL_REASON to "reason",
                    QualificationMapper.KEY_REVIEWED_BY to "someone",
                    QualificationMapper.KEY_IS_ACCEPTED to true
            )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(qualificationFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(qualificationFixture, mapper.unmarshal(mapFixture ))
    }

}