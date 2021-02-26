package reach52.marketplace.community.persistence.insurance

import reach52.marketplace.community.models.DocumentMeta
import reach52.marketplace.community.persistence.InsuranceDocumentMetaMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class InsuranceDocumentMetaMapperTest {

    private val mapper = InsuranceDocumentMetaMapper()

    private val documentMetaFixture = DocumentMeta(

            dateCreated = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            createdBy = "Malayan",
            organization = "sample org",
            source = "malayan source",
            type = "insurancePlan"
    )

    private val mapFixture = mapOf(
            InsuranceDocumentMetaMapper.KEY_DATE_CREATED to "1970-01-01T00:00:00Z",
            InsuranceDocumentMetaMapper.KEY_CREATED_BY to "Malayan",
            InsuranceDocumentMetaMapper.KEY_ORGANIZATION to "sample org",
            InsuranceDocumentMetaMapper.KEY_SOURCE to "malayan source",
            InsuranceDocumentMetaMapper.KEY_TYPE to "insurancePlan"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(documentMetaFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(documentMetaFixture, mapper.unmarshal(mapFixture))
    }
}