package reach52.marketplace.community.insurance.entity

data class InsuranceProduct(
		val id: String,
		val catCode: String,
		val catNo: String,
		val insurerCode: String, // suppCode
		val suppCatNo: String,
		val displayName: String,
		val displaySummary: String,
		val minAge: Float,// age is float to accomodate days along with years
		val maxAge: Float,
		val parties: HashMap<String, Party>,
		val constructs: ArrayList<Construct>,
		val maxBeneficiaries: Int = 1,
		val benefits: ArrayList<Benefit>,
		val generalExclusions: List<String>,
		val tax: Tax,
		val term: Int,
		val locale: List<String>,
		val isoCountry: String,
		val isoCurrency: String,
		val questionsURL: String? = null
) {

	data class Party(
			val code: String,
			val displayName: String,
			val premium: HashMap<String, Premium>,
			val minAge: Float = 1F,
			val maxAge: Float = 100F
	)

	data class ConstructParty(
			val party: Party,
			val minQty: Int,
			val maxQty: Int
	){
		override fun toString(): String {
			return party.displayName
		}
	}

	data class Tax(
			val isIncluded: Boolean = false,
			val percentage: Float,
			val type: String
	)

	data class Construct(
			val code: String,
			val displayName: String,
			val premium: HashMap<String, Premium>,
			val isoCurrency: String,
			val extraParties: ArrayList<ConstructParty>
	)

	data class Benefit(
			val displayName: String,
			val displayDesc: String,
			val exclusions: String,
			val totalInsured: String,
			val isoCurrency: String
	)

	data class Exclusion(
			val displayText: String
	)

}