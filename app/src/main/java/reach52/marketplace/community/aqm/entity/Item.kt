package reach52.marketplace.community.aqm.entity

abstract class Item(
		val id: String,
		val question: String,
		val optional: Boolean
){

	companion object{
		const val TEXT_ITEM = 1
		const val SINGLE_SELECT_ITEM = 2
		const val MULTI_SELECT_ITEM = 3
	}

	data class Option(
			val id: String,
			val text: String
	)

}