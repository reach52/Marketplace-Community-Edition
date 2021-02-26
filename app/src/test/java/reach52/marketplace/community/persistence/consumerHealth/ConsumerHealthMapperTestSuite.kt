package reach52.marketplace.community.persistence.consumerHealth

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        CategoryMapperTest::class,
        ConsumerHealthOrderMapperTest::class,
        CreatedByMapperTest::class,
        OrderLinesMapperTest::class,
        ProductsMapperTest::class,
        ServiceOrProductMapperTest::class,
        ShoppingCartMapperTest::class)
class ConsumerMapperTestSuite