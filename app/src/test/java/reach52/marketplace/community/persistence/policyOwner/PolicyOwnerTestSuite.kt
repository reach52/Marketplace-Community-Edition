package reach52.marketplace.community.persistence.policyOwner

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        BeneficiariesMapperTest::class,
        FileMapperTest::class,
        InsuredMapperTest::class,
        InsurerMapperTest::class,
        PolicyOwnerMapperTest::class,
        QualificationMapperTest::class,
        UpdatedByMapperTest::class
)
class PolicyOwnerTestSuite