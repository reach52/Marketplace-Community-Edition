package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.policy_owner.File
import reach52.marketplace.community.persistence.policyOwner_mapper.FileMapper
import org.junit.Assert
import org.junit.Test

class FileMapperTest {

    private val mapper = FileMapper()
    private val fileFixture = File(
            contentType = "contentType",
            digest = "digest",
            length = 1.00,
            repos = 1,
            stub = true
    )

    private val mapFixture = mapOf(
            FileMapper.KEY_CONTENT_TYPE to "contentType",
            FileMapper.KEY_DIGEST to "digest",
            FileMapper.KEY_LENGTH to 1.00,
            FileMapper.KEY_REPOS to 1,
            FileMapper.KEY_STUB to true
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(fileFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(fileFixture, mapper.unmarshal(mapFixture))
    }
}