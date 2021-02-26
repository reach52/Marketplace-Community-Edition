package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.EffectiveDate
import reach52.marketplace.community.persistence.EffectiveDateMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class EffectiveDateMapperTest {

    private val effectiveDateModelFixture = EffectiveDate(
            dateStart = ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
            dateEnd = ZonedDateTime.of(2020, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC)
    )

    private val effectiveDateMapFixture = mapOf(
            EffectiveDateMapper.KEY_EFFECTIVE_DATE_START to "2019-07-05T00:00:00Z",
            EffectiveDateMapper.KEY_EFFECTIVE_DATE_END to "2020-07-05T00:00:00Z"
    )

    @Test
    fun unmarshal() {
        assertEquals(effectiveDateModelFixture, EffectiveDateMapper().unmarshal(effectiveDateMapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(effectiveDateMapFixture, EffectiveDateMapper().marshal(effectiveDateModelFixture))
    }
}