package edu.bluejack20_2.braven.services

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageMediaService @Inject constructor() {
    fun createIntent(): Intent {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        return chooserIntent
    }

    fun bitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap =
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                contentResolver, uri
            )
        )

    fun byteArrayFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }
}