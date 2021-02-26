package reach52.marketplace.community.fragments

import android.app.Activity
import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import arrow.core.Some
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Purchases
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_take_photo_insurance.view.*
import java.io.File
import java.util.*

@ExperimentalUnsignedTypes
class AcknowledgementTakePhotoFragment : androidx.fragment.app.Fragment() {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0x1
    }

    private val disposables = CompositeDisposable()
    private lateinit var mInsuranceViewModel: Option<InsuranceViewModel>
    private var isImageCaptured = false

    private val residentLogisticViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val purchase by lazy {
        Purchases(DispergoDatabase.getInstance(context!!))
    }

//    private val user by lazy {
//        Auth.currentSession(context!!).map {
//            it.user
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mInsuranceViewModel = Option.fromNullable(activity).map {
            ViewModelProvider(it)[InsuranceViewModel::class.java]
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_take_photo_insurance, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.acknowledgment)

//            disposables.add(completePurchaseButton.clicks().subscribe({
//                when {
//                    user.isEmpty() ->
//                        Toast.makeText(context, getString(R.string.text_session_expired), Toast.LENGTH_LONG).show()
//
//                    isImageCaptured -> {
//
//                        binding {
//                            val insuranceViewModel = mInsuranceViewModel.bind()
//                            val acknowledgementPhoto = insuranceViewModel.acknowledgementPhoto.bind()
//                            val coveredFamilyMembers = Option.fromNullable(insuranceViewModel.coveredFamilyMembers.value).bind()
//                            val insurance = Option.fromNullable(insuranceViewModel.selectedInsurance.value).bind()
//                            val insuranceID = Option.fromNullable(insuranceViewModel.selectedInsuranceID.value).bind()
//                            val plan = Option.fromNullable(insuranceViewModel.selectedInsuranceInsurancePlan.value).bind()
//                            val insuredID: String
//
//                            if (residentLogisticViewModel.isLogisticResident.value == true) {
//                                val logisticResident = Option.fromNullable(insuranceViewModel.selectedLogisticResident.value).bind()
//                                insuredID = purchase.residentLogistic(
//                                        acknowledgementPhoto = acknowledgementPhoto,
//                                        plan = plan,
//                                        insuranceID = insuranceID,
//                                        insurance = insurance,
//                                        coveredFamilyMembers = coveredFamilyMembers,
//                                        logisticResident = logisticResident,
//                                        user = user
//                                )
//                            } else {
//                                val resident = Option.fromNullable(insuranceViewModel.selectedResident.value).bind()
//                                insuredID = purchase.residentAccess(
//                                        acknowledgementPhoto = acknowledgementPhoto,
//                                        plan = plan,
//                                        insuranceID = insuranceID,
//                                        insurance = insurance,
//                                        coveredFamilyMembers = coveredFamilyMembers,
//                                        resident = resident,
//                                        user = user
//                                )
//                            }
//                            val purchased = purchase.get(insuredID)
//                            insuranceViewModel.purchasedInsurance.value = purchased
//                            insuranceViewModel.resetVM()
//                            parentFragmentManager
//                                    .beginTransaction()
//                                    .replace(R.id.fragment_container, PurchasePreviewFragment.newInstance(insuredID))
//                                    .addToBackStack(null)
//                                    .commit()
//                        }
//                    }
//
//                    else ->
//                        Toast.makeText(context, getString(R.string.text_acknowledgement_required), Toast.LENGTH_SHORT).show()
//                }
//            }, {
//                throw NotImplementedError()
//            }))


            disposables.add(takePhotoButton.clicks().subscribe({
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                    intent.resolveActivity(activity!!.packageManager)?.also {
                        createTempFile().let { file ->
                            mInsuranceViewModel.map {
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
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    mInsuranceViewModel.flatMap { it.acknowledgementPhoto }.map { insuredPhoto ->
//                        BitmapFactory.decodeFile(insuredPhoto)
//                                ?.also(capturedImageView::setImageBitmap)
//                    }
//                    isImageCaptured = true
//                    takePhotoButton.isClickable = false
//                }
                Activity.RESULT_CANCELED -> isImageCaptured = isImageCaptured || false
            }
        }
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
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                FileProvider.getUriForFile(
                        context!!,
                        "healthcare.alliedworld.dispergo.FileProvider",
                        file
                )
            } else {
                Uri.fromFile(file)
            }
}
