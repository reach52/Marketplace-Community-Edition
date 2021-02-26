package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.AgeRange
import reach52.marketplace.community.persistence.Unmarshaler

class AgeRangeMapper : Unmarshaler<AgeRange>{
    companion object {
        const val KEY_MIN = "min"
        const val KEY_MAX = "max"
    }

    override fun unmarshal(properties: Map<String, Any>): AgeRange {
        return AgeRange(
                min = (properties[KEY_MIN] as Number).toInt(),
                max = (properties[KEY_MAX] as Number).toInt()
        )
    }
}
