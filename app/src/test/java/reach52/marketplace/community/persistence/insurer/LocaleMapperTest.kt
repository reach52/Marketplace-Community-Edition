package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Locale
import reach52.marketplace.community.models.insurance.Pricing
import reach52.marketplace.community.persistence.insurer_mapper.LocaleMapper
import reach52.marketplace.community.persistence.insurer_mapper.PricingMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class LocaleMapperTest {
    private val localeModelFixture = Locale(
            pricing = Pricing(
                    text = "Philippine peso",
                    unit = "Php."
            ),
            identifier = "PH",
            languages = listOf(
                    "Tagalog",
                    "English"
            )
    )

    private val localeMapFixture = mapOf(
            LocaleMapper.KEY_PRICING to mapOf(
                    PricingMapper.KEY_TEXT to "Philippine peso",
                    PricingMapper.KEY_UNIT to "Php."
            ),
            LocaleMapper.KEY_IDENTIFIER to "PH",
            LocaleMapper.KEY_LANGUAGES to listOf(
                    "Tagalog",
                    "English"
            )
    )

    @Test
    fun unmarshal() {
        assertEquals(localeModelFixture, LocaleMapper().unmarshal(localeMapFixture))
    }
}