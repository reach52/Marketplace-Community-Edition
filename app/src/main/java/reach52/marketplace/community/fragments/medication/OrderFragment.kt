package reach52.marketplace.community.fragments.medication

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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Discount
import reach52.marketplace.community.models.medication.Physician
import reach52.marketplace.community.models.medication.Physicians
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.OrderViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_consumer_health_discount_selection_saved.*

import java.io.File
import java.util.*

@ExperimentalUnsignedTypes
class OrderFragment : androidx.fragment.app.Fragment() {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0x1
    }

    private val disposables = CompositeDisposable()

    private lateinit var discounts: Map<String, Option<Discount>>
    private lateinit var physicians: Map<String, Option<Physician>>
    private lateinit var model: Option<OrderViewModel>
    private var isImageCaptured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val db = DispergoDatabase.getInstance(activity!!.applicationContext)

        physicians = Physicians(db).all().toList().map {
            Pair(String.format("%s - %s", it.record.licenseNumber, it.record.name), Some(it.record))
        }.toMap()

        model = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java].also { orderViewModel ->
                discounts = orderViewModel.discounts.map { discount ->
                            Pair<String, Option<Discount>>(discount.code, Some(discount))
                        }.startWith(Pair<String, Option<Discount>>("Discount (Optional)", None)).toList()
                        .blockingGet().toMap()
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_extra_data_fragment, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title =
                    getString(R.string.prescription_photo)
            discountCodeSpinner.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    discounts.keys.toTypedArray()
            )
            physicianLicenseSpinner.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    physicians.keys.toTypedArray()
            )

            disposables.add(discountCodeSpinner.itemSelections().subscribe({
                model.map { orderViewModel ->
                    orderViewModel.selectedDiscount =
                            discounts.getValue(discountCodeSpinner.getItemAtPosition(it) as String)
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(physicianLicenseSpinner.itemSelections().subscribe({
                model.map { orderViewModel ->
                    if (physicianLicenseSpinner.count > 0) {
                        orderViewModel.selectedPhysician =
                                physicians.getValue(physicianLicenseSpinner.getItemAtPosition(it) as String)
                    }
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(discountIdNumberTextInputEditText.textChanges().subscribe({
                model.map { orderViewModel ->
                    orderViewModel.discountIdNumber = if (it.isNullOrBlank()) {
                        None
                    } else {
                        Some(it.toString())
                    }
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(prescriptionNumberEditText.textChanges().subscribe({
                model.map { orderViewModel ->
                    orderViewModel.prescriptionNumber = if (it.isNullOrBlank()) {
                        None
                    } else {
                        Some(it.toString())
                    }
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(orderMedicineButton.clicks().subscribe({
                if (isImageCaptured) {
                    parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, MedicationSuppliersFragment())
                            .addToBackStack(null)
                            .commit()
                } else {
                    Toast.makeText(
                            context,
                            context.getString(R.string.required_photo_prescription),
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(takePhotoButton.clicks().subscribe({
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                    intent.resolveActivity(activity!!.packageManager)?.also {
                        createTempFile().let { file ->
                            model.map {
                                it.photoPath = Some(file.absolutePath)
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
                Activity.RESULT_OK -> {
                    model.flatMap { it.photoPath }.map { photoPath ->
                        BitmapFactory.decodeFile(photoPath)?.also(capturedImageView::setImageBitmap)
                    }
                    isImageCaptured = true
                }
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
