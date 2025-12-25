package com.yousful.snapsweepai.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

object PerceptualHashUtil {

    fun computeHash(
        context: Context,
        uri: Uri
    ): Long? {
        return try {
            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.RGB_565
                inSampleSize = 32   // 🔥 BIG SPEED BOOST
            }

            val bitmap = context.contentResolver
                .openInputStream(uri)
                ?.use { BitmapFactory.decodeStream(it, null, options) }
                ?: return null

            val resized = Bitmap.createScaledBitmap(bitmap, 8, 8, true)
            bitmap.recycle()

            var sum = 0
            val gray = IntArray(64)

            for (i in 0 until 64) {
                val x = i % 8
                val y = i / 8
                val pixel = resized.getPixel(x, y)
                val v =
                    ((pixel shr 16) and 0xff +
                            (pixel shr 8) and 0xff +
                            (pixel) and 0xff) / 3
                gray[i] = v
                sum += v
            }

            val avg = sum / 64
            var hash = 0L
            for (i in gray.indices) {
                if (gray[i] >= avg) hash = hash or (1L shl i)
            }

            resized.recycle()
            hash
        } catch (e: Exception) {
            null
        }
    }


    fun hammingDistance(a: Long, b: Long): Int =
        java.lang.Long.bitCount(a xor b)
}
