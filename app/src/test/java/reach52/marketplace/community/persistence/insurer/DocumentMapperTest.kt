package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.*
import reach52.marketplace.community.persistence.insurer_mapper.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class DocumentMapperTest {

    private val documentModelFixture = InsurerDocument(
            identifier = "MLN",
            display = "Malayan",
            type = "insurer",
            summary = "This is a sample brief description for Malayan Insurance Company.",
            contact = Contact(
                    phone = "+639123456780",
                    email = "mailto@malayan.com",
                    address = listOf(
                            Address(
                                    line = listOf("line","some_line"),
                                    city = "Manila",
                                    country = "Philippines",
                                    state = "State 03",
                                    zip = "1800"
                            )
                    )
            ),
            locale = Locale(
                    pricing = Pricing(
                            text = "Philippine peso",
                            unit = "Php."
                    ),
                    identifier = "PH",
                    languages = listOf(
                            "Tagalog",
                            "English"
                    )
            ),
            formulary = listOf(
                    Formulary(
                            identifier = "MALAYAN_BASIC_PLAN",
                            tier = "basic",
                            summary = "This is a sample description for Malayan Basic tier.",
                            category = "adult",
                            rate = 100.0,
                            beneficiary = 1,
                            benefits = listOf(
                                    Benefit(
                                            category = "Accidental Death",
                                            amount = 10000.0,
                                            identifier = "ACCIDENTAL_DEATH"
                                    )
                            ),
                            ageRange = AgeRange(
                                    min = 18,
                                    max = 65
                            )
                    )
            ),
            policy = Policy(
                    text = "<h2>This is a sample insurance policy for Malayan. /n <h2>",
                    url = "https://malayan.com.ph/insurance-policy"
            ),
            requirements = listOf(
                    Requirement(
                            identifier = "GOVERNMENT_ID",
                            category = "Any Government ID",
                            option = true,
                            variations = listOf(
                                    "SSS",
                                    "TIN",
                                    "PHIL-HEALTH"
                            ),
                            references = listOf(
                                    "MALAYAN_BASIC_PLAN",
                                    "MALAYAN_SILVER_PLAN",
                                    "MALAYAN_BRONZE_PLAN",
                                    "MALAYAN_GOLD_PLAN",
                                    "MALAYAN_PLATINUM_PLAN"
                            )
                    )
            ),
            qualification = Qualification(
                    questionnaire = Questionnaire(
                            url = "https://kc.reach52malayan.qualification/forms",
                            koboUser = "koboUser",
                            reference = "AlDYIxWE-12213-SDW3-RW3E32-ADS2"
                    )
            ),
            dateLastUpdated = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            updatedBy = UpdatedBy(
                    reference = "test reference",
                    display = "test@gmail.com"
            ),
            organizationType = "Non profit organization"
    )

    private val documentMapFixture = mapOf(
            InsurerDocumentMapper.KEY_IDENTIFIER to "MLN",
            InsurerDocumentMapper.KEY_DISPLAY to "Malayan",
            InsurerDocumentMapper.KEY_TYPE to "insurer",
            InsurerDocumentMapper.KEY_SUMMARY to "This is a sample brief description for Malayan Insurance Company.",
            InsurerDocumentMapper.KEY_CONTACT to mapOf(
                    ContactMapper.KEY_PHONE to "+639123456780",
                    ContactMapper.KEY_EMAIL to "mailto@malayan.com",
                    ContactMapper.KEY_ADDRESS to listOf(
                            mapOf(
                                    AddressMapper.KEY_LINE to listOf("line","some_line"),
                                    AddressMapper.KEY_CITY to "Manila",
                                    AddressMapper.KEY_COUNTRY to "Philippines",
                                    AddressMapper.KEY_STATE to "State 03",
                                    AddressMapper.KEY_ZIP to "1800"
                            )
                    )
            ),
            InsurerDocumentMapper.KEY_LOCALE to mapOf(
                    LocaleMapper.KEY_PRICING to mapOf(
                            PricingMapper.KEY_TEXT to "Philippine peso",
                            PricingMapper.KEY_UNIT to "Php."
                    ),
                    LocaleMapper.KEY_IDENTIFIER to "PH",
                    LocaleMapper.KEY_LANGUAGES to listOf(
                            "Tagalog",
                            "English"
                    )
            ),
            InsurerDocumentMapper.KEY_FORMULARY to listOf(
                    mapOf(
                            FormularyMapper.KEY_IDENTIFIER to "MALAYAN_BASIC_PLAN",
                            FormularyMapper.KEY_TIER to "basic",
                            FormularyMapper.KEY_SUMMARY to "This is a sample description for Malayan Basic tier.",
                            FormularyMapper.KEY_CATEGORY to "adult",
                            FormularyMapper.KEY_RATE to 100.0,
                            FormularyMapper.KEY_BENEFICIARY to 1,
                            FormularyMapper.KEY_BENEFITS to listOf(
                                    mapOf(
                                            BenefitsMapper.KEY_CATEGORY to "Accidental Death",
                                            BenefitsMapper.KEY_AMOUNT to 10000.0,
                                            BenefitsMapper.KEY_IDENTIFIER to "ACCIDENTAL_DEATH"
                                    )
                            ),
                            FormularyMapper.KEY_AGE_RANGE to mapOf(
                                    AgeRangeMapper.KEY_MIN to 18,
                                    AgeRangeMapper.KEY_MAX to 65
                            )
                    )
            ),
            InsurerDocumentMapper.KEY_POLICY to mapOf(
                    PolicyMapper.KEY_TEXT to "<h2>This is a sample insurance policy for Malayan. /n <h2>",
                    PolicyMapper.KEY_URL to "https://malayan.com.ph/insurance-policy"
            ),
            InsurerDocumentMapper.KEY_REQUIREMENTS to listOf(
                    mapOf(
                            RequirementsMapper.KEY_IDENTIFIER to "GOVERNMENT_ID",
                            RequirementsMapper.KEY_CATEGORY to "Any Government ID",
                            RequirementsMapper.KEY_OPTION to true,
                            RequirementsMapper.KEY_VARIATIONS to listOf(
                                    "SSS",
                                    "TIN",
                                    "PHIL-HEALTH"
                            ),
                            RequirementsMapper.KEY_REFERENCES to listOf(
                                    "MALAYAN_BASIC_PLAN",
                                    "MALAYAN_SILVER_PLAN",
                                    "MALAYAN_BRONZE_PLAN",
                                    "MALAYAN_GOLD_PLAN",
                                    "MALAYAN_PLATINUM_PLAN"
                            )
                    )
            ),
            InsurerDocumentMapper.KEY_QUALIFICATION to mapOf(
                    QualificationMapper.KEY_QUESTIONNAIRE to mapOf(
                            QuestionnaireMapper.KEY_URL to "https://kc.reach52malayan.qualification/forms",
                            QuestionnaireMapper.KEY_KOBO_USER to "koboUser",
                            QuestionnaireMapper.KEY_REFERENCE to "AlDYIxWE-12213-SDW3-RW3E32-ADS2"
                    )
            ),
            InsurerDocumentMapper.KEY_LAST_UPDATED to "1970-01-01T00:00:00Z",
            InsurerDocumentMapper.KEY_UPDATED_BY to mapOf(
                    UpdatedByMapper.KEY_REFERENCE to "test reference",
                    UpdatedByMapper.KEY_DISPLAY to "test@gmail.com"
            ),
            InsurerDocumentMapper.KEY_ORGANIZATION_TYPE to "Non profit organization"
    )

    @Test
    fun unmarshal() {
        assertEquals(documentModelFixture, InsurerDocumentMapper().unmarshal(documentMapFixture))
    }
}