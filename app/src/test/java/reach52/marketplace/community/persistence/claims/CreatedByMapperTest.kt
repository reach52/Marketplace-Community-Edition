package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.CreatedBy
import reach52.marketplace.community.persistence.claims_mapper.ClaimsCreatedByMapper
import org.junit.Assert
import org.junit.Test

class CreatedByMapperTest {
    private val createdByModelFixture = CreatedBy(
            identifier = "identifier",
            username = "username",
            role = "role",
            email = "email"
    )

    private val createdByMapFixture = mapOf(
            ClaimsCreatedByMapper.KEY_CREATED_BY_ID to "identifier",
            ClaimsCreatedByMapper.KEY_CREATED_BY_USERNAME to "username",
            ClaimsCreatedByMapper.KEY_CREATED_BY_ROLE to "role",
            ClaimsCreatedByMapper.KEY_CREATED_BY_EMAIL to "email"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(createdByModelFixture, ClaimsCreatedByMapper().unmarshal(createdByMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(createdByMapFixture, ClaimsCreatedByMapper().marshal(createdByModelFixture))
    }
}