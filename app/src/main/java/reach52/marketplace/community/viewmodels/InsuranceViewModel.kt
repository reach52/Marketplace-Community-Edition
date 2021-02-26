package reach52.marketplace.community.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import arrow.core.None
import arrow.core.Option
import reach52.marketplace.community.models.*
import reach52.marketplace.community.models.insurance.Benefit
import reach52.marketplace.community.models.insurance.Formulary
import reach52.marketplace.community.models.insurance.InsurerDocument
import reach52.marketplace.community.models.insurance.Requirement
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.models.policy_owner.Beneficiaries
import reach52.marketplace.community.models.policy_owner.PolicyOwner
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import reach52.marketplace.community.models.policy_owner.Insured as PolicyOwnerInsured
import reach52.marketplace.community.models.policy_owner.Qualification as PolicyOwnerQualification

class InsuranceViewModel : ViewModel() {
    var acknowledgementPhoto: Option<String> = None
    var coveredFamilyMembers: MutableLiveData<MutableList<CoverageInsured>> = MutableLiveData(mutableListOf()) // {:brother, "mike chu"}
    var purchasedInsurance: MutableLiveData<Insured> = MutableLiveData()
    var selectedInsurance: MutableLiveData<InsuranceDomainResource> = MutableLiveData() // malayan
    var selectedInsuranceID: MutableLiveData<String> = MutableLiveData()
    var selectedInsuranceInsurancePlan: MutableLiveData<InsurancePlan> = MutableLiveData() // bronze, silver, gold
    var selectedResident: MutableLiveData<Resident> = MutableLiveData() // selected resident
    var selectedLogisticResident: MutableLiveData<LogisticResident> = MutableLiveData()

    //New Insurance view model
    var selectedInsurerId: MutableLiveData<String> = MutableLiveData()
    var selectedInsurer: MutableLiveData<InsurerDocument> = MutableLiveData()
    var selectedInsurerPlan: MutableLiveData<Formulary> = MutableLiveData()
    var selectedInsurerRequirement: MutableLiveData<Requirement> = MutableLiveData()
    var addedBeneficiaries: MutableLiveData<MutableList<Beneficiaries>> = MutableLiveData(mutableListOf())
    var policyOwnerQualification: MutableLiveData<PolicyOwnerQualification> = MutableLiveData()
    var policyOwnerInsured: MutableLiveData<PolicyOwnerInsured> = MutableLiveData()
    var policyOwner: MutableLiveData<PolicyOwner> = MutableLiveData()
    var policyOwnerID : MutableLiveData<String> = MutableLiveData()
    var policyOwnerFullName : MutableLiveData<String> = MutableLiveData()
    var policyOwnerAddress : MutableLiveData<String> = MutableLiveData()
    var policyOwnerEmail : MutableLiveData<String> = MutableLiveData()
    var policyOwnerContact : MutableLiveData<String> = MutableLiveData()
    var policyOwnerCivilStatus : MutableLiveData<String> = MutableLiveData()
    var policyOwnerGender : MutableLiveData<String> = MutableLiveData()
    var policyOwnerDateOfBirth : MutableLiveData<ZonedDateTime> = MutableLiveData()
    var policyOwnerDocumentId: MutableLiveData<String> = MutableLiveData()
    var unit : MutableLiveData<String> = MutableLiveData()
    var mapDataQuestionnaire : MutableLiveData<Map<String, Any>> = MutableLiveData()

    //New LiveData
    var selectedPolicyOwnerFullName : LiveData<String> = Transformations.map(policyOwnerFullName){it}
    var selectedPolicyOwnerAddress : LiveData<String> = Transformations.map(policyOwnerAddress){it}
    var selectedPolicyOwnerEmail : LiveData<String> = Transformations.map(policyOwnerEmail){it}
    var selectedPolicyOwnerContact : LiveData<String> = Transformations.map(policyOwnerContact){it}
    var selectedPolicyOwnerCivilStatus : LiveData<String> = Transformations.map(policyOwnerCivilStatus){it}
    var selectedPolicyOwnerGender : LiveData<String> = Transformations.map(policyOwnerGender){it}
    var selectedPolicyOwnerDateOfBirth : LiveData<ZonedDateTime> = Transformations.map(policyOwnerDateOfBirth){it}

    val selectedBeneficiaries: LiveData<List<Beneficiaries>> = Transformations.map(addedBeneficiaries){ it }
    val selectedPolicyInsured: LiveData<PolicyOwnerInsured> = Transformations.map(policyOwnerInsured){ it }
    val insuranceName: LiveData<String> = Transformations.map(selectedInsurer){ it.display }
    val selectedInsurerFormularyRate: LiveData<Double> = Transformations.map(selectedInsurerPlan) { it.rate }
    val selectedInsurerFormularyTitle: LiveData<String> = Transformations.map(selectedInsurerPlan) { it.tier }
    val selectedInsurerFormularyBenefits: LiveData<List<Benefit>> = Transformations.map(selectedInsurerPlan) { it.benefits }

    //Existing LiveData
    val selectedInsurancePlanExpiryDate: LiveData<LocalDateTime> = Transformations.map(selectedInsuranceInsurancePlan) {
        LocalDateTime.now().plusDays(Duration.ofMillis(it.dateExpiry).toDays())
    }

    val dependents = ArrayList<Dependent>()

    val selectedInsurancePlanPrice: LiveData<Double> = Transformations.map(selectedInsuranceInsurancePlan) { it.price }
    val selectedInsurancePlanSpecificCosts: LiveData<List<SpecificCost>> = Transformations.map(selectedInsuranceInsurancePlan) { it.specificCosts }
    val selectedInsurancePlanTitle: LiveData<String> = Transformations.map(selectedInsuranceInsurancePlan) { it.title }

    fun resetVM() {
        selectedLogisticResident = MutableLiveData()
        coveredFamilyMembers.value = mutableListOf()
        acknowledgementPhoto = None
        dependents.clear()
    }

    fun resetBeneficiaries() {
        addedBeneficiaries.value = mutableListOf()
    }

    fun resetQuestionnaire() {
        mapDataQuestionnaire.value = mutableMapOf()
    }
}