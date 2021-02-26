package reach52.marketplace.community.extensions

import android.graphics.Bitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun Bitmap.compressToInputStream(format: Bitmap.CompressFormat, quality: Int): InputStream {
    val outputStream = ByteArrayOutputStream()
    this.compress(format, quality, outputStream)
    return ByteArrayInputStream(outputStream.toByteArray())
}

fun Bitmap.toWebpStream(quality: Int): InputStream =
        this.compressToInputStream(Bitmap.CompressFormat.WEBP, quality)

fun Bitmap.toPhotoAttachmentStream(): InputStream = this.toWebpStream(80)