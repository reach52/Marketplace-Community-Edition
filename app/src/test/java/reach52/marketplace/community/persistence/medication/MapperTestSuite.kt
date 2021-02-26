package reach52.marketplace.community.persistence.medication

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        DiscountMapperTest::class,
        MedicationMapperTest::class,
        OrderItemMapperTest::class,
        OrderMapperTest::class,
        OrderStatusMapperTest::class,
        PhysicianMapperTest::class,
        ResidentMapperTest::class,
        SuppliersMapperTest::class
)
class MapperTestSuite