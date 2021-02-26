package reach52.marketplace.community.aqm.entity

class BoolItem(
		id: String,
		question: String,
		optional: Boolean) : Item(id, question, optional) {

	var value: Boolean? = null

}