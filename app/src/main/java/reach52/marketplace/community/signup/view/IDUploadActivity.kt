package reach52.marketplace.community.signup.view

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.DispergoApp
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.extensions.utils.ImageUtils.compressImage
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.signup.RegistrationHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_id_upload.*
import java.io.File

class IDUploadActivity : BaseActivity() {

	private val REQUEST_IMAGE_CAPTURE = 1

	private val imageFile = File(DispergoApp.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ID.png")

	private val disposables = CompositeDisposable()

	private lateinit var dialog: AlertDialog

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_id_upload)

		dialog = AlertDialog.Builder(this)
				.setMessage(R.string.uploading_image)
				.create()

		next_btn.setOnClickListener { v ->
			v.isEnabled = false
			dialog.show()
			disposables.add(RegistrationHelper().uploadPhotoID(imageFile).subscribe(
					{
						dialog.dismiss()
						startActivity(Intent(this, IntroVideoActivity::class.java))
					},
					{
						v.isEnabled = true
						dialog.dismiss()
						when (it) {
							is RegistrationHelper.ImageTooLargeException -> {
								Toast.makeText(this, resources.getString(R.string.image_too_large_error), Toast.LENGTH_SHORT).show()
							}
							is RegistrationHelper.IDUploadFailException -> {
								Toast.makeText(this, resources.getString(R.string.id_image_upload_fail), Toast.LENGTH_SHORT).show()
							}
							is NetworkManager.NoInternetConnectionException -> {
								Toast.makeText(this, resources.getString(R.string.you_are_offline), Toast.LENGTH_SHORT).show()
							}
							else -> {
								Toast.makeText(this, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
							}
						}
						FirebaseCrashlytics.getInstance().recordException(it)
						it.printStackTrace()
					}
			))
		}

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == REQUEST_IMAGE_CAPTURE) {

			if (resultCode == Activity.RESULT_OK) {
				updateViewsBeforeProcessing()
				disposables.add(
						compressImage(imageFile, 500000)
								.subscribe(
										{

											processing_progress_bar.progress = it

										},
										{
											Toast.makeText(this, R.string.image_processing_fail, Toast.LENGTH_SHORT).show()
											updateViewsOnProcessingFinish()
											id_img.visibility = View.GONE
											id_img_placeholder.visibility = View.VISIBLE
										},
										{

											updateViewsOnProcessingFinish()
											id_img.setImageBitmap(BitmapFactory.decodeFile(imageFile.absolutePath))
										})
				)
			}
		}

	}

	private fun updateViewsBeforeProcessing(){
		progress_message.visibility = View.VISIBLE
		processing_progress_bar.visibility = View.VISIBLE
		processing_progress_bar.progress = 0
		id_img.visibility = View.INVISIBLE
		id_img_placeholder.visibility = View.GONE
		next_btn.isEnabled = false
		upload_id_btn.isEnabled = false
	}

	private fun updateViewsOnProcessingFinish(){
		progress_message.visibility = View.INVISIBLE
		processing_progress_bar.visibility = View.INVISIBLE
		id_img.visibility = View.VISIBLE
		next_btn.isEnabled = true
		upload_id_btn.isEnabled = true
	}

	fun onUploadID(v: View) {

		dispatchTakePictureIntent()

	}

	override fun onDestroy() {
		super.onDestroy()
		disposables.dispose()
	}

	private fun dispatchTakePictureIntent() {
		val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		if (takePictureIntent.resolveActivity(packageManager) != null) {

			val uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.FileProvider", imageFile)
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

		}
	}

}