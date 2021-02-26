package reach52.marketplace.community.medicine.view

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.ImageUtils
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel
import kotlinx.android.synthetic.main.fragment_prescription.*
import kotlinx.android.synthetic.main.fragment_prescription.view.*

class PrescriptionFragment : Fragment() {

	val REQUEST_IMAGE_CAPTURE = 32

	private lateinit var vm: MedicationPurchaseViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		val activity = activity as MedicationPurchaseActivity
		vm = ViewModelProvider(activity)[MedicationPurchaseViewModel::class.java]

		return inflater.inflate(R.layout.fragment_prescription, container, false).apply {

			activity.dispo.add(prescriptionNumberEditText.textChanges().subscribe({
				vm.prescriptionNumber = it.toString()
			}, {
				throw NotImplementedError()
			}))

			takePhotoButton.setOnClickListener {
				activity.dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE)
			}

			saveButton.setOnClickListener {

				try {
					vm.validatePrescription()
					activity.onBackPressed()
				} catch (e: Exception) {
					when (e) {
						is MedicationPurchaseViewModel.PrescriptionNumberMissingException -> {
							Toast.makeText(context, context.getString(R.string.prescription_required), Toast.LENGTH_SHORT).show()
						}
						is MedicationPurchaseViewModel.PrescriptionImageMissingException -> {
							Toast.makeText(context, context.getString(R.string.required_photo_prescription), Toast.LENGTH_SHORT).show()
						}
					}
				}

			}

		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == REQUEST_IMAGE_CAPTURE) {
			when (resultCode) {
				Activity.RESULT_OK -> {

					(activity as MedicationPurchaseActivity).dispo.add(
							ImageUtils.compressImage(vm.prescriptionImageFile, 1000000).subscribe(
									{

									},
									{

									},
									{
										capturedImageView.setImageBitmap(BitmapFactory.decodeFile(vm.prescriptionImageFile.absolutePath))
									}
							)
					)

				}
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)

	}
	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.action_cart).isVisible = false
	}

	override fun onResume() {
		super.onResume()
		(activity as MedicationPurchaseActivity).supportActionBar?.title = getString(R.string.prescription_photo)
	}


}