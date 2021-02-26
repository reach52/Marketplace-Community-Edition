package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Qualification
import reach52.marketplace.community.models.insurance.Questionnaire
import reach52.marketplace.community.persistence.insurer_mapper.QualificationMapper
import reach52.marketplace.community.persistence.insurer_mapper.QuestionnaireMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class QualificationMapperTest {
    private val qualificationModelFixture = Qualification(
            questionnaire = Questionnaire(
                    url = "",
                    koboUser = "",
                    reference = ""
            )
    )

    private val qualificationMapFixture = mapOf(
            QualificationMapper.KEY_QUESTIONNAIRE to mapOf(
                    QuestionnaireMapper.KEY_URL to "",
                    QuestionnaireMapper.KEY_KOBO_USER to "",
                    QuestionnaireMapper.KEY_REFERENCE to ""
            )
    )

    @Test
    fun unmarshal() {
        assertEquals(qualificationModelFixture, QualificationMapper().unmarshal(qualificationMapFixture))
    }
}