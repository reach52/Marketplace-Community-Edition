package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.DocumentMeta
import reach52.marketplace.community.persistence.DocumentMetaMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class DocumentMetaMapperTest {

    private val insuredDocumentMetaModelFixture = DocumentMeta(
            dateCreated = ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
            createdBy = "name",
            organization = "organization",
            source = "source",
            type = "type"
    )

    private val mapFixture = mapOf(
            DocumentMetaMapper.KEY_DOCUMENT_META_DATE_CREATED to "2019-07-05T00:00:00Z",
            DocumentMetaMapper.KEY_DOCUMENT_META_CREATED_BY to "name",
            DocumentMetaMapper.KEY_DOCUMENT_META_ORGANIZATION to "organization",
            DocumentMetaMapper.KEY_DOCUMENT_META_SOURCE to "source",
            DocumentMetaMapper.KEY_DOCUMENT_META_TYPE to "type"
    )

    @Test
    fun unmarshal() {
        assertEquals(insuredDocumentMetaModelFixture, DocumentMetaMapper().unmarshal(mapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(mapFixture, DocumentMetaMapper().marshal(insuredDocumentMetaModelFixture))
    }
}