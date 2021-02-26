package reach52.marketplace.community.persistence.insured

import arrow.core.None
import arrow.core.Option
import reach52.marketplace.community.models.insured.AddressPeriod
import reach52.marketplace.community.persistence.AddressPeriodMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class AddressPeriodMapperTest {

    private val mapper = AddressPeriodMapper()

    private val periodFixture = AddressPeriod(
            start = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            end = Option.fromNullable(ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
    )

    private val periodEmptyEndFixture =
            AddressPeriod(
                    start = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    end = None
            )

    private val mapFixture = mapOf(
            "start" to "1970-01-01T00:00:00Z",
            "end" to "1970-01-01T00:00:00Z"
    )

    private val mapEmptyEndFixture = mapOf("start" to "1970-01-01T00:00:00Z")


    @Test
    fun marshal() {
        assertEquals(mapFixture, mapper.marshal(periodFixture))
    }

    @Test
    fun marshalWhenEmptyEndPeriod() {
        assertEquals(mapEmptyEndFixture, mapper.marshal(periodEmptyEndFixture))
    }

    @Test
    fun unmarshal() {
        assertEquals(periodFixture, mapper.unmarshal(mapFixture))
    }

    @Test
    fun unmarshalWhenEmptyEndPeriod() {
        assertEquals(periodEmptyEndFixture, mapper.unmarshal(mapEmptyEndFixture))
    }

}