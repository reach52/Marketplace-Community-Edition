package reach52.marketplace.community.persistence.insurance

import arrow.core.Some
import reach52.marketplace.community.models.Company
import reach52.marketplace.community.models.insured.Address
import reach52.marketplace.community.persistence.AddressMapper
import reach52.marketplace.community.persistence.CompanyInsuranceMapper
import org.junit.Assert
import org.junit.Test

class CompanyInsuranceMapperTest {

    private val mapper = CompanyInsuranceMapper()

    private val companyFixture = Company(
            identifier = "id123",
            name = "Malayan",
            type = "health",
            telecommunication = "tel123",
            address = listOf(Address(
                    city = Some("Manila City"),
                    country = Some("PH"),
                    district = Some("District 1"),
                    line = listOf("123"),
                    postalCode = Some("1601"),
                    state = Some("Region 1"))
            )
    )

    private val mapFixture = mapOf(
            CompanyInsuranceMapper.KEY_IDENTIFIER to "id123",
            CompanyInsuranceMapper.KEY_NAME to "Malayan",
            CompanyInsuranceMapper.KEY_TYPE to "health",
            CompanyInsuranceMapper.KEY_TELECOMMUNICATION to "tel123",
            CompanyInsuranceMapper.KEY_ADDRESS to listOf(
                    mapOf(
                            AddressMapper.KEY_CITY to "Manila City",
                            AddressMapper.KEY_COUNTRY to "PH",
                            AddressMapper.KEY_DISTRICT to "District 1",
                            AddressMapper.KEY_LINE to listOf("123"),
                            AddressMapper.KEY_POSTAL_CODE to "1601",
                            AddressMapper.KEY_STATE to "Region 1"
                    )
            )
    )


    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(companyFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(companyFixture, mapper.unmarshal(mapFixture))
    }

}