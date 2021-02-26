package reach52.marketplace.community.aqm.entity

class SingleSelectItem(
		id: String,
		question: String,
		optional: Boolean,
		val options: List<Option>
) : Item(id, question, optional) {

	var value: Option? = null

}