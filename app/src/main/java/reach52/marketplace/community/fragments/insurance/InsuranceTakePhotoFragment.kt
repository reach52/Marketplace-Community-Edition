package reach52.marketplace.community.fragments.insurance

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import arrow.core.Some
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.insurance.InsurancePurchase
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_take_photo.*
import kotlinx.android.synthetic.main.fragment_insurance_take_photo.view.*
import java.io.File
import java.util.*

class InsuranceTakePhotoFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0x1
    }

    private val disposables = CompositeDisposable()

    private val insuranceViewModel by lazy {
        Option.fromNullable(activity).map {
            ViewModelProvider(it)[InsuranceViewModel::class.java]
        }
    }
    private val insuranceViewModelGetter by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private var isImageCaptured = false


    private val insurancePurchase by lazy {
        InsurancePurchase(DispergoDatabase.getInstance(context!!))
    }

//    private val user by lazy {
//        Auth.currentSession(context!!).map {
//            it.user
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insurance_take_photo, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.acknowledgement_initials)

            if(insuranceViewModelGetter.selectedInsurerRequirement.value?.option == true){
                certificateNumberInputLayout.visibility = View.VISIBLE
            }
            disposables.add(completePurchaseButton.clicks().subscribe {
//                when {
//                    user.isEmpty() -> Toast.makeText(context, getString(R.string.text_session_expired), Toast.LENGTH_LONG).show()
//                    isImageCaptured -> {
//                        when (certificateNumberEditText.text.toString().isEmpty() && insuranceViewModelGetter.selectedInsurerRequirement.value?.option == true){
//                            true ->{
//                                certificateNumberInputLayout.error = "Please input certificate number"
//                            }
//                            false -> {
//                                if(certificateNumberEditText.text.toString().take(1) == " "){
//                                    Toast.makeText(context, "Invalid input: space is not allowed as first input", Toast.LENGTH_LONG).show()
//                                } else {
//                                    when(insuranceViewModelGetter.policyOwnerDocumentId.value != null){
//                                        true->{
//                                            binding {
//                                                val viewModel = insuranceViewModel.bind()
//                                                val acknowledgementPhoto = viewModel.acknowledgementPhoto.bind()
//                                                val beneficiaries = Option.fromNullable(viewModel.addedBeneficiaries.value).bind()
//                                                val insured = Option.fromNullable(viewModel.policyOwnerInsured.value).bind()
//                                                insurancePurchase.updatePolicy(
//                                                        insured = insured,
//                                                        policyOwnerId = insuranceViewModelGetter.policyOwnerDocumentId.value.toString(),
//                                                        beneficiaries = beneficiaries,
//                                                        acknowledgementPhoto = acknowledgementPhoto,
//                                                        status = "pending issuance"
//                                                )
//                                            }
//                                        }
//                                        false->{
//                                            binding {
//                                                val viewModel = insuranceViewModel.bind()
//                                                val acknowledgementPhoto = viewModel.acknowledgementPhoto.bind()
//                                                val beneficiaries = Option.fromNullable(viewModel.addedBeneficiaries.value).bind()
//                                                val insured = Option.fromNullable(viewModel.policyOwnerInsured.value).bind()
//                                                val insuranceId = Option.fromNullable(viewModel.selectedInsurerId.value).bind()
//                                                val insurer = Option.fromNullable(viewModel.selectedInsurer.value).bind()
//                                                val formulary = Option.fromNullable(viewModel.selectedInsurerPlan.value).bind()
//                                                val certificateNumber = Option.fromNullable(certificateNumberEditText.text.toString()).bind()
//                                                val insurerName = Option.fromNullable(viewModel.insuranceName.value).bind()
//                                                val policyOwnerID = Option.fromNullable(viewModel.policyOwnerID.value).bind()
//                                                val policyOwnerFullName = Option.fromNullable(viewModel.policyOwnerFullName.value).bind()
//                                                val policyOwnerAddress = Option.fromNullable(viewModel.policyOwnerAddress.value).bind()
//                                                val policyOwnerEmail = Option.fromNullable(viewModel.policyOwnerEmail.value).bind()
//                                                val policyOwnerContact = Option.fromNullable(viewModel.policyOwnerContact.value).bind()
//                                                val policyOwnerCivilStatus = Option.fromNullable(viewModel.policyOwnerCivilStatus.value).bind()
//                                                val policyOwnerGender = Option.fromNullable(viewModel.policyOwnerGender.value).bind()
//                                                val policyOwnerDateOfBirth = Option.fromNullable(viewModel.policyOwnerDateOfBirth.value).bind()
//                                                val unit = Option.fromNullable(viewModel.unit.value).bind()
//                                                val status : String
//
//                                                status = when(insurer.qualification.questionnaire.url == ""){
//                                                    true -> {
//                                                        "active"
//                                                    }
//                                                    false ->{
//                                                        "pending issuance"
//                                                    }
//                                                }
//
//                                                insurancePurchase.addPurchase(
//                                                        insurerName = insurerName,
//                                                        acknowledgementPhoto = acknowledgementPhoto,
//                                                        beneficiaries = beneficiaries,
//                                                        insured = insured,
//                                                        policyOwnerID = policyOwnerID,
//                                                        policyOwnerFullName = policyOwnerFullName,
//                                                        policyOwnerAddress = policyOwnerAddress,
//                                                        policyOwnerEmail = policyOwnerEmail,
//                                                        policyOwnerContact = policyOwnerContact,
//                                                        policyOwnerCivilStatus = policyOwnerCivilStatus,
//                                                        policyOwnerGender = policyOwnerGender,
//                                                        policyOwnerDateOfBirth = policyOwnerDateOfBirth,
//                                                        insuranceId = insuranceId,
//                                                        formulary = formulary,
//                                                        certificateNumber = certificateNumber,
//                                                        user = user,
//                                                        unit = unit,
//                                                        answers = Option.fromNullable(viewModel.mapDataQuestionnaire.value).bind(),
//                                                        status = status,
//                                                        dependents = viewModel.dependents
//                                                )
//                                            }
//                                        }
//                                    }
//                                    parentFragmentManager.popBackStack("residentDetails", POP_BACK_STACK_INCLUSIVE)
//                                    insuranceViewModelGetter.resetBeneficiaries()
//                                    insuranceViewModelGetter.resetQuestionnaire()
//                                }
//                            }
//                        }
//                    }
//                else ->
//                    Toast.makeText(context, getString(R.string.text_acknowledgement_required), Toast.LENGTH_SHORT).show()
//                }
            })

            disposables.add(takePhotoButton.clicks().subscribe({
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                    intent.resolveActivity(activity!!.packageManager)?.also {
                        createTempFile().let { file ->
                            insuranceViewModel.map {
                                it.acknowledgementPhoto = Some(file.absolutePath)
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(file))
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            }, {
                throw NotImplementedError()
            }))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            when (resultCode) {
                Activity.RESULT_OK -> {
                    insuranceViewModel.flatMap { it.acknowledgementPhoto }.map { insuredPhoto ->
                        BitmapFactory.decodeFile(insuredPhoto)
                                ?.also(capturedImageView::setImageBitmap)
                    }
                    isImageCaptured = true
                    takePhotoButton.isClickable = false
                }
                Activity.RESULT_CANCELED -> isImageCaptured = isImageCaptured || false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    private fun createTempFile() = File.createTempFile(
            UUID.randomUUID().toString(),
            ".jpg",
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )

    private fun getUriForFile(file: File): Uri? =
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
                FileProvider.getUriForFile(
                        context!!,
                        "healthcare.alliedworld.dispergo.FileProvider",
                        file
                )
            } else {
                Uri.fromFile(file)
            }
}