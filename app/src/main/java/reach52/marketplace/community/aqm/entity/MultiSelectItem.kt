package reach52.marketplace.community.aqm.entity

import kotlinx.serialization.Serializable

@Serializable
class MultiSelectItem(
		id: String,
		question: String,
		optional: Boolean,
		val options: List<Option>
) : Item(id, question, optional) {

	var value = ArrayList<Option>()

}