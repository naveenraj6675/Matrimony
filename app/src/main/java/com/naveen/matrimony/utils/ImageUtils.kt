package com.naveen.matrimony.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.Closeable
import java.io.IOException
import java.io.OutputStream

object ImageUtils {

    fun normalizeImageForUri(context: Context, uri: Uri, initialPath: String) {
        try {
            val imagePath = initialPath + "/" + uri.lastPathSegment
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val `is` = context.contentResolver.openInputStream(uri)

            val bitmap = BitmapFactory.decodeStream(`is`)

            var rotatedBitmap = rotateBitmap(bitmap, orientation)

            var scaledBitmap: Bitmap? = null
            if (rotatedBitmap != null)
                scaledBitmap = Bitmap.createScaledBitmap(
                    rotatedBitmap,
                    rotatedBitmap.width / 2,
                    rotatedBitmap.height / 2,
                    false
                )
            else
                scaledBitmap =
                    Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, false)

            saveBitmapToFile(context, scaledBitmap, uri, initialPath, orientation)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        try {
            val bmRotated =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()

            return bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }

    }

    private fun saveBitmapToFile(
        context: Context,
        croppedImage: Bitmap?,
        saveUri: Uri?,
        initialPath: String,
        exifOrientation: Int
    ) {
        if (saveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = context.contentResolver.openOutputStream(saveUri)
                if (outputStream != null) {
                    croppedImage!!.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }

                if (exifOrientation != 0) {
                    val imagePath = initialPath + "/" + saveUri.lastPathSegment
                    val newExif = ExifInterface(imagePath)
                    newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation.toString())
                    newExif.saveAttributes()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                closeSilently(outputStream)
                croppedImage!!.recycle()
            }
        }
    }


    private fun closeSilently(c: Closeable?) {
        if (c == null) return
        try {
            c.close()
        } catch (t: Throwable) {
            // Do nothing
        }

    }


}