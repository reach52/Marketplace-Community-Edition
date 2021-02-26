package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Questionnaire
import reach52.marketplace.community.persistence.insurer_mapper.QuestionnaireMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class QuestionnaireMapperTest {
    private val questionnaireModelFixture = Questionnaire(
            url = "",
            koboUser = "",
            reference = ""
    )

    private val questionnaireMapFixture = mapOf(
            QuestionnaireMapper.KEY_URL to "",
            QuestionnaireMapper.KEY_KOBO_USER to "",
            QuestionnaireMapper.KEY_REFERENCE to ""
    )

    @Test
    fun unmarshal() {
        assertEquals(questionnaireModelFixture, QuestionnaireMapper().unmarshal(questionnaireMapFixture))
    }

}