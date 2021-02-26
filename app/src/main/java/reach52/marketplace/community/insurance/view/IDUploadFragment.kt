package reach52.marketplace.community.insurance.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import kotlinx.android.synthetic.main.fragment_id_upload.*

class IDUploadFragment : Fragment() {


	val REQUEST_IMAGE_CAPTURE = 12

	lateinit var vmr: ResidentViewModel
	private lateinit var ctx: Context
	private lateinit var vm: InsurancePurchaseViewModel

	private lateinit var saveMenu: MenuItem
	private lateinit var dialog: AlertDialog

	override fun onAttach(context: Context) {
		super.onAttach(context)
		this.ctx = context
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.save).let {
			it.isVisible = vm.idImageFile.length() > 0
			it.setOnMenuItemClickListener {
				it.isEnabled = false
				dialog.show()
				vm.uploadIDImage(ctx).subscribe(
						{
							dialog.dismiss()
							(activity as InsurancePurchaseActivity).gotoOrderPreview()
						},
						{ err ->
							dialog.dismiss()
							it.isEnabled = true
							Toast.makeText(ctx, err.message, Toast.LENGTH_SHORT).show()
						}
				)
				true
			}
			saveMenu = it
		}
		menu.findItem(R.id.home_action).let {
			it.isVisible = true

			it.setOnMenuItemClickListener {
				try {

					(activity as InsurancePurchaseActivity).gotoOrderPreviewhome(context!!)
				} catch (e: java.lang.Exception) {
					System.out.println("onclick home= " +  e.message)
				}
				true
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val activity = activity as InsurancePurchaseActivity

		vm = ViewModelProvider(activity)[InsurancePurchaseViewModel::class.java]
		vmr = ViewModelProvider(activity)[ResidentViewModel::class.java]
		dialog = AlertDialog.Builder(activity)
				.setMessage(R.string.uploading_image)
				.create()
		return inflater.inflate(R.layout.fragment_id_upload, container, false)

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val activity = activity as InsurancePurchaseActivity
		upload_id_btn.setOnClickListener {
			activity.dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE)
		}

	}

	override fun onResume() {
		super.onResume()

		val file = vm.idImageFile

		if (::saveMenu.isInitialized) {
			saveMenu.isVisible = file.length() > 0
		}
		if (file.length() > 0) {
			progress_message.visibility = View.INVISIBLE
			processing_progress_bar.visibility = View.INVISIBLE
			id_placeholder.visibility = View.GONE
			id_img.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
		}
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.id_upload)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == REQUEST_IMAGE_CAPTURE) {

			val dispo = (activity as InsurancePurchaseActivity).dispo

			if (resultCode == Activity.RESULT_OK) {
				updateViewsBeforeProcessing()
				dispo.add(
						vm.compressIDImage().subscribe(
								{

									processing_progress_bar.progress = it

								},
								{
									Toast.makeText(ctx, R.string.image_processing_fail, Toast.LENGTH_SHORT).show()
									updateViewsOnProcessingFinish()
									id_img.visibility = View.GONE
									id_placeholder.visibility = View.VISIBLE
								},
								{

									updateViewsOnProcessingFinish()
									vm.uploadedIDURL = ""
									id_img.setImageBitmap(BitmapFactory.decodeFile(vm.idImageFile.absolutePath))
								})
				)
			}
		}

	}

	private fun updateViewsBeforeProcessing() {
		progress_message.visibility = View.VISIBLE
		processing_progress_bar.visibility = View.VISIBLE
		processing_progress_bar.progress = 0
		id_img.visibility = View.INVISIBLE
		id_placeholder.visibility = View.GONE
		upload_id_btn.isEnabled = false
	}

	private fun updateViewsOnProcessingFinish() {
		progress_message.visibility = View.INVISIBLE
		processing_progress_bar.visibility = View.INVISIBLE
		id_img.visibility = View.VISIBLE
		upload_id_btn.isEnabled = true
	}

	//click on home button 16-12-2020
/*	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			R.id.home_action -> {

				try {
					activity?.startActivity(Intent(activity, ResidentDetailsActivity::class.java).apply {
						this.putExtra(ResidentDetailsActivity.KEY_ID, vmr.residentId)
					})
				}catch(e : java.lang.Exception)
				{
					System.out.println("onclick home= "+vmr.residentId+" "+e.message)
				}


				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}*/

}