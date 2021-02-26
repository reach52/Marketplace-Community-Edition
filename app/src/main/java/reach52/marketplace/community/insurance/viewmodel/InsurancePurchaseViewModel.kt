package reach52.marketplace.community.insurance.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.DispergoApp
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.extensions.utils.ImageUtils
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.extensions.utils.calculateAgeFromDOB
import reach52.marketplace.community.extensions.utils.isPhoneInvalid
import reach52.marketplace.community.insurance.entity.*
import reach52.marketplace.community.insurance.repo.InsuranceProductRepo
import reach52.marketplace.community.insurance.repo.InsurerRepo
import reach52.marketplace.community.insurance.repo.OrderRepo
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.repo.ResidentRepo
import io.reactivex.Completable
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.io.File

@SuppressLint("CheckResult")
class InsurancePurchaseViewModel : ViewModel() {

	lateinit var resident: Resident
	val insurers = MutableLiveData<ArrayList<Insurer>>()
	lateinit var selectedInsurer: Insurer
	val insuranceProducts = MutableLiveData<ArrayList<InsuranceProduct>>()
	lateinit var selectedInsuranceProduct: InsuranceProduct
	lateinit var selectedConstruct: InsuranceProduct.Construct
	val selectedMembers = ArrayList<Member>()
	val premiumPeriod = MutableLiveData<String>()
	var totalPremium: Float = 0F
	var finalPremium: Float = 0F
	val breakdownPrimaryMembers = ArrayList<Member>()
	val breakdownExtraMembers = ArrayList<Member>()
	val beneficiaries = ArrayList<Beneficiary>()
	val idImageFile by lazy { File(DispergoApp.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "insuranceID.png") }
	var uploadedIDURL: String = ""
	lateinit var answers: HashMap<String, String>

	init {
		if (idImageFile.exists()) {
			idImageFile.delete()
		}
	}

	fun loadInsurers(context: Context) {

		InsurerRepo.getAllInsurers(context).subscribe(
				{
					insurers.value = it
				},
				{

				}
		)

	}

	fun loadInsuranceProducts(context: Context, insurer: Insurer) {

		InsuranceProductRepo.getInsuranceProductsFromInsurer(context, insurer.code, LanguageUtils.getSavedLanguageInISO3()).subscribe(
				{
					insuranceProducts.value = it
				},
				{

				}
		)

	}

	fun loadResident(context: Context, id: String) = Completable.create {

		ResidentRepo.fetchResident(context, id).subscribe(
				{ r ->
					resident = r
					it.onComplete()
				},
				{

				}
		)

	}

	fun saveMembers() = Completable.create { emitter ->

		val countMap = HashMap<InsuranceProduct.ConstructParty, Int>()
		selectedConstruct.extraParties.forEach {
			countMap[it] = 0
		}

		breakdownPrimaryMembers.clear()
		breakdownExtraMembers.clear()

		val period = selectedConstruct.premium.keys.iterator().next()

		selectedMembers.forEach {

			val party = it.constructParty
			if (countMap.containsKey(party)) {
				countMap[party] = (countMap[party] as Int) + 1
			} else {
				countMap[party] = 1
			}

			val code = it.constructParty.party.code
			val amount = it.constructParty.party.premium[period]!!.amount

			if (code == "primary" || amount <= 0F) {
				breakdownPrimaryMembers.add(it)
			} else {
				breakdownExtraMembers.add(it)
			}

		}

		for ((party, count) in countMap) {

			if (count > party.maxQty) {
				emitter.onError(TooManyMembersException("Maximum ${party.maxQty} ${party.party.displayName} are allowed"))
				return@create
			}
			if (count < party.minQty) {
				emitter.onError(TooFewMembersException("add at least ${party.minQty} ${party.party.displayName}"))
				return@create
			}

		}

		emitter.onComplete()

	}

	fun saveBeneficiaries() = Completable.create {

		val maxBeneficiaries = selectedInsuranceProduct.maxBeneficiaries

		if (beneficiaries.size > maxBeneficiaries) {
			it.onError(TooManyBeneficiariesException("Too many bens"))
			return@create
		}

		it.onComplete()

	}

	fun getPrimaryParty(vm: InsurancePurchaseViewModel): InsuranceProduct.ConstructParty {

		return InsuranceProduct.ConstructParty(
				vm.selectedInsuranceProduct.parties["primary"]!!,
				1,
				1
		)

	}

	fun calculateTotalPremium(period: String): Float {

		totalPremium = selectedConstruct.premium[period]!!.amount
		selectedMembers.forEach {
			totalPremium += it.constructParty.party.premium[period]!!.amount
		}
		return totalPremium

	}

	fun calculateTotalPremiumWithTax(): Float {

		val taxPercentage = selectedInsuranceProduct.tax.percentage
		val taxableAmount = (taxPercentage * totalPremium) / 100
		finalPremium = totalPremium + taxableAmount
		return finalPremium

	}

	fun calculateExtraPremium(period: String): Float {
		var total = 0F
		breakdownExtraMembers.forEach {
			total += it.constructParty.party.premium[period]!!.amount
		}
		return total
	}

	fun validateMember(member: Member) {

		if (member.firstName.isEmpty()) {
			throw EmptyNameException()
		}
		if (member.lastName.isEmpty()) {
			throw EmptyNameException()
		}

		val maxAge = member.constructParty.party.maxAge
		val minAge = member.constructParty.party.minAge
		val age = calculateAgeFromDOB(member.DOB)

		if (minAge < 1) {
			val days = member.DOB.until(ZonedDateTime.now(), ChronoUnit.DAYS)
			val minDays = (minAge * 365).toInt()
			if (days < minDays) {
				throw AgeTooLowException("$minDays days")
			}
		}

		if (age < minAge) {
			throw AgeTooLowException(minAge.toInt().toString())
		}

		if (age > maxAge) {
			throw AgeTooHighException(maxAge.toInt().toString())
		}

	}

	fun validateBeneficiary(beneficiary: Beneficiary) {

		if (beneficiary.firstName.isEmpty()) {
			throw EmptyNameException()
		}
		if (beneficiary.lastName.isEmpty()) {
			throw EmptyNameException()
		}

		if (isPhoneInvalid(beneficiary.phone)) {
			throw InvalidPhoneException()
		}

		if (
				beneficiary.addressLine1.isEmpty() ||
				beneficiary.city.isEmpty() ||
				beneficiary.country.isEmpty() ||
				beneficiary.zipCode.isEmpty()
		) {
			throw IncompleteAddressException()
		}

	}

	fun compressIDImage() = ImageUtils.compressImage(idImageFile, 500000)

	fun uploadIDImage(context: Context) = Completable.create {

		if (uploadedIDURL.isEmpty()) {
			OrderRepo.uploadIDImage(idImageFile, context).subscribe(
					{ url ->
						uploadedIDURL = url
						it.onComplete()
					},
					{ err ->
						it.onError(err)
					}
			)
		} else {
			it.onComplete()
		}

	}

	fun saveOrder(context: Context): Completable {

		val payment = PolicyPurchase.Payment(
				finalPremium,
				premiumPeriod.value!!
		)

		if (!::answers.isInitialized) {
			answers = HashMap<String, String>()
		}

		val order = PolicyPurchase(
				selectedInsurer,
				selectedInsuranceProduct,
				selectedMembers,
				beneficiaries,
				payment,
				uploadedIDURL,
				resident,
				answers,
				LocalUserRepo.getUser(context)
		)

		return OrderRepo.createNewPolicyOrder(context, order)

	}

	fun saveOrderRequest(context: Context): Completable {

		val order = PolicyPurchase(
				selectedInsurer,
				selectedInsuranceProduct,
				ArrayList(),
				ArrayList(),
				null,
				null,
				resident,
				answers,
				LocalUserRepo.getUser(context)
		)

		return OrderRepo.createNewPolicyOrder(context, order)

	}

	fun isResidentEligible(product: InsuranceProduct) {

		val age = resident.getAge()
		val minAge = product.parties["primary"]!!.minAge
		val maxAge = product.parties["primary"]!!.maxAge
		if (age < minAge) {
			throw AgeTooLowException(minAge.toInt().toString())
		}

		if (age > maxAge) {
			throw AgeTooHighException(maxAge.toInt().toString())
		}

	}

	class InvalidPhoneException : Exception()
	class EmptyNameException : Exception()
	class IncompleteAddressException : Exception()
	class AgeTooLowException(message: String) : Exception(message)
	class AgeTooHighException(message: String) : Exception(message)

	class TooManyBeneficiariesException(message: String) : Exception(message)
	class TooManyMembersException(message: String) : Exception(message)
	class TooFewMembersException(message: String) : Exception(message)

}