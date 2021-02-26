package reach52.marketplace.community.persistence.insurance

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        BenefitInsuranceMapperTest::class,
        CompanyInsuranceMapperTest::class,
        CoverageInsuranceMapperTest::class,
        InsuranceDocumentMetaMapperTest::class,
        InsuranceDomainResourceMapperTest::class,
        InsuranceMapperTest::class,
        InsuranceInsurancePlanMapperTest::class,
        RequirementsInsuranceMapperTest::class,
        SpecificCostMapperTest::class)
class InsuranceMapperTestSuite