package reach52.marketplace.community.auth.entity

class LocalUser(
		val id: String,
		val email: String,
		val username: String,
		val firstName: String,
		val lastName: String,
		val country: String,
		val documents: ArrayList<Document> = ArrayList(),
		val catalogTags: ArrayList<String> = ArrayList()
){

	val displayName: String = listOf(firstName, lastName)
			.filter(String::isNotBlank)
			.joinToString(" ")

}

