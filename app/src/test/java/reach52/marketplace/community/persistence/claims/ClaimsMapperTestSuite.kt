package reach52.marketplace.community.persistence.claims

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        BenefitClaimsMapperTest::class,
        BenefitsMapperTest::class,
        CreatedByMapperTest::class,
        ClaimsCurrentStatusMapperTest::class,
        ClaimsInsurancePoliciesMapperTest::class,
        ClaimsPastStatusesMapperTest::class,
        SubmittedBasicRequirementsMapperTest::class,
        SubmittedRequirementsMapperTest::class,
        ClaimsMapperTest::class
)
class ClaimsMapperTestSuite