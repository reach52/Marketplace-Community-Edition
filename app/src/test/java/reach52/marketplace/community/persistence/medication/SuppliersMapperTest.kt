package reach52.marketplace.community.persistence.medication

import reach52.marketplace.community.models.medication.Suppliers
import reach52.marketplace.community.persistence.medication_mapper.SuppliersMapper
import org.junit.Assert
import org.junit.Test

class SuppliersMapperTest {

    private val mapper = SuppliersMapper()
    private val supplierFixture = Suppliers(
            id = "UUID",
            identifier = "medExpressSupplier",
            supplierName = "MedExpress",
            type = "medicationSupplier"
    )

    private val mapFixture = mapOf(
            SuppliersMapper.KEY_ID to "UUID",
            SuppliersMapper.KEY_IDENTIFIER to "medExpressSupplier",
            SuppliersMapper.KEY_SUPPLIER_NAME to "MedExpress",
            SuppliersMapper.KEY_TYPE to "medicationSupplier"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(supplierFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(supplierFixture, mapper.unmarshal(mapFixture))
    }
}