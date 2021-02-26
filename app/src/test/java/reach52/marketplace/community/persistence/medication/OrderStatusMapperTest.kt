package reach52.marketplace.community.persistence.medication

import arrow.core.None
import reach52.marketplace.community.models.medication.OrderStatus
import reach52.marketplace.community.persistence.OrderStatusMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class OrderStatusMapperTest {
    private val mapper = OrderStatusMapper()

    private val orderStatusFixture = OrderStatus(
            status = OrderStatus.Status.ACCEPTED,
            statusDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            username = None,
            userDisplayName = None,
            userId = None,
            reason = None
    )

    private val mapFixture = mapOf(
            OrderStatusMapper.KEY_STATUS to "ACCEPTED",
            OrderStatusMapper.KEY_STATUS_DATE to "1970-01-01T00:00:00Z"
    )

    @Test
    fun marshal() {
        assertEquals(mapFixture, mapper.marshal(orderStatusFixture))
    }

    @Test
    fun unmarshal() {
        assertEquals(orderStatusFixture, mapper.unmarshal(mapFixture))
    }
}