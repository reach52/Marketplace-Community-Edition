package reach52.marketplace.community.extensions.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageUtils {

	fun compressImage(file: File, MAX_FILE_SIZE: Long = 2 * 1000000.toLong()) = Observable.create<Int> {
		val inputBitmap = BitmapFactory.decodeFile(file.absolutePath)

		try {
			var fileSize: Long = file.length()
			Log.v("taaag", "original file size : $fileSize")
			var MAX_QUALITY = 90

			val total_diff = MAX_FILE_SIZE - fileSize
			it.onNext(0)
			while (fileSize > MAX_FILE_SIZE) {

				val diff = MAX_FILE_SIZE - fileSize
				val progress = total_diff - diff
				val progess_percentage = (progress * 100) / total_diff

				val fOut: OutputStream = FileOutputStream(file)
				inputBitmap.compress(Bitmap.CompressFormat.JPEG, MAX_QUALITY, fOut)
				fOut.close()
				fileSize = file.length()
				Log.v("taaag", "file size after: $fileSize at quality: $MAX_QUALITY")
				if (MAX_QUALITY > 0) {
					MAX_QUALITY -= 5
				} else {
					break
				}

				it.onNext(progess_percentage.toInt())

			}
			it.onNext(100)
			it.onComplete()
		} catch (e: Exception) {
			e.printStackTrace()
			it.onError(e)
		}
	}
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())

}