package reach52.marketplace.community.aqm.entity

import kotlinx.serialization.Serializable

@Serializable
class TextItem(
		id: String,
		question: String,
		optional: Boolean,
		val inputType: String
) : Item(id, question, optional){

	var value: String? = null

	enum class InputType{
		Name, Number, Email, Address, Phone
	}

	companion object{
		const val NAME = "name"
		const val TEXT = "text"
		const val NUMBER = "number"
		const val PHONE = "phone"
		const val EMAIL = "email"
		const val ADDRESS = "address"
	}

}