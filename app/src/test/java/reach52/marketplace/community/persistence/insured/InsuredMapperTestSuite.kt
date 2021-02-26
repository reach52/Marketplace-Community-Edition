package reach52.marketplace.community.persistence.insured

import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        AddressMapperTest::class,
        BenefitInsuredMapperTest::class,
        CoverageInsuredMapperTest::class,
        EffectiveDateMapperTest::class,
        InsuredDomainResourceMapperTest::class,
        InsuredStatusMapperTest::class,
        InsurancePlanReferenceMapperTest::class,
        RequirementReferenceMapperTest::class,
        SpecificCostMapperTest::class,
        DocumentMetaMapperTest::class,
        AddressPeriodMapperTest::class,
        InsuredMapperTest::class,
        InsuredContactMapperTest::class
)

class InsuredMapperTestSuite