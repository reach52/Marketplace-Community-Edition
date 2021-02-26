package reach52.marketplace.community.signup.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.DispergoApp
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.auth.view.LoginActivity
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.extensions.utils.ImageUtils.compressImage
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.extensions.utils.createDatePicker
import reach52.marketplace.community.signup.viewmodel.RegistrationViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_user_details_input.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : BaseActivity() {

	private val country = CountryManager.getCountryCode()
	private val countryPhoneCode = CountryManager().getCountryPhoneCode(country)

	lateinit var vm: RegistrationViewModel
	private val disposables = CompositeDisposable()
	private val REQUEST_IMAGE_CAPTURE = 23

	private val imageFile = File(DispergoApp.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile.png")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_user_details_input)
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

		vm = ViewModelProvider(this)[RegistrationViewModel::class.java]
		vm.init(this)

		if (imageFile.exists()) {
			imageFile.delete()
		}

		setupUI()

		back_btn.setOnClickListener {
			super.onBackPressed()
		}

	}

	@SuppressLint("SimpleDateFormat")
	private fun setupUI() {

		val dateFormat = SimpleDateFormat("yyyy-MM-dd")
		dob.setOnClickListener {

			val datePicker = createDatePicker(this,
					DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
						run {
							val cal = Calendar.getInstance()
							cal.set(year, month, dayOfMonth)
							vm.user.DOB = dateFormat.format(cal.time)

							dob.setText("$dayOfMonth/${month + 1}/$year")
						}
					}
			)
			datePicker.show()

		}

		std_code.text = CountryManager().getCountryPhoneCode(country)

		val photoClickListener = View.OnClickListener {
			dispatchTakePictureIntent()
		}

		profilePic.setOnClickListener(photoClickListener)
		addPicBtn.setOnClickListener(photoClickListener)

		TnC_checkBox.setOnCheckedChangeListener { _, isChecked ->
			next_btn.isEnabled = isChecked
		}
		TnC_text.setOnClickListener {
			startActivity(Intent(this, FAQActivity::class.java).apply {
				putExtra(FAQActivity.KEY_MODE, FAQActivity.KEY_TnC)
			})
		}
		privacy_policy.setOnClickListener {
			startActivity(Intent(Intent.ACTION_VIEW).apply {
				val url = "https://reach52.com/about/consent/"
				data = Uri.parse(url)
			})
		}

	}

	fun onNextPress(v: View) {

		if (!imageFile.exists()) {
			Toast.makeText(this, "Please capture image.", Toast.LENGTH_SHORT).show()
			return
		}

		vm.user.firstName = first_name.text.toString()
		vm.user.lastName = last_name.text.toString()
		vm.user.gender = gender.selectedItem.toString()
		vm.user.country = country
		vm.user.phone = phone.text.toString()
		vm.user.region = region.text.toString()
		vm.user.town = town.text.toString()
		vm.user.profilePic = imageFile

		next_btn.isEnabled = false

		val progressDialog = ProgressDialog(this).apply {
			setCancelable(false)
		}

		progressDialog.show()
		progressDialog.setMessage("Registering...")
		dispo.add(
				vm.register(this).subscribe({

					progressDialog.dismiss()
					Log.i("taaag", "registration done.")
					startActivity(Intent(this, IDUploadActivity::class.java))

				}, { error ->
					progressDialog.dismiss()
					next_btn.isEnabled = true

					when (error) {
						is NetworkManager.InvalidRegistrationDataException -> {
							Toast.makeText(this, "Invalid registration data", Toast.LENGTH_SHORT).show()
						}
					}
					Log.e("taaag", "registration error: $error")
					Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
				})

		)


	}

	private fun gotoLogin() {
		val intent = Intent(this, LoginActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		startActivity(intent)
	}

	private fun dispatchTakePictureIntent() {
		val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		if (takePictureIntent.resolveActivity(packageManager) != null) {

			val uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.FileProvider", imageFile)
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
			takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == REQUEST_IMAGE_CAPTURE) {

			if (resultCode == Activity.RESULT_OK) {
				disposables.add(
						compressImage(imageFile, 500000)
								.subscribe(
										{

										},
										{
											Toast.makeText(this, R.string.image_processing_fail, Toast.LENGTH_SHORT).show()
										},
										{
											profilePic.setImageBitmap(BitmapFactory.decodeFile(imageFile.absolutePath))
										}
								)
				)
			}
		}

	}

}