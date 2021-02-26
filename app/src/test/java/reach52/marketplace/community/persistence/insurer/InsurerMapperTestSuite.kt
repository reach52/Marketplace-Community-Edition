package reach52.marketplace.community.persistence.insurer

import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        AddressMapperTest::class,
        BenefitsMapperTest::class,
        ContactMapperTest::class,
        DocumentMapperTest::class,
        FormularyMapperTest::class,
        LocaleMapperTest::class,
        PolicyMapperTest::class,
        PricingMapperTest::class,
        QualificationMapperTest::class,
        QuestionnaireMapperTest::class,
        RequirementsMapperTest::class,
        UpdatedByMapperTest::class
)

class InsurerMapperTestSuite