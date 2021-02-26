package reach52.marketplace.community.medicine.mapper

import reach52.marketplace.community.medicine.entity.Physician
import reach52.marketplace.community.persistence.Unmarshaler

@Suppress("UNCHECKED_CAST")
class PhysicianMapper: Unmarshaler<Physician>{

    companion object {
        private val physicianNameMapper = PhysicianNameMapper()
    }

    override fun unmarshal(properties: Map<String, Any>): Physician {
        val id = properties["_id"] as String
        val licenseNumber = properties["licenseNumber"] as String
        val physicianName = physicianNameMapper.unmarshal(properties["physicianName"] as Map<String, Any>)
        val medicalSpecialty = properties["medicalSpecialty"] as String

        return Physician(
                id,
                licenseNumber,
                physicianName,
                medicalSpecialty
        )
    }
    class PhysicianNameMapper: Unmarshaler<Physician.Name>{

        override fun unmarshal(properties: Map<String, Any>): Physician.Name {
            val givenName: String = properties["givenName"] as String
            val familyName: String = properties["familyName"] as String
            val middleName: String = properties["middleName"] as String

            return Physician.Name(
                    givenName,
                    familyName,
                    middleName
            )
        }

    }
}