package com.yousful.snapsweepai.logic

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object OcrTextExtractor {

    private val recognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun extractText(
        context: Context,
        uri: Uri
    ): String = suspendCancellableCoroutine { cont ->

        try {
            val image = InputImage.fromFilePath(context, uri)

            recognizer.process(image)
                .addOnSuccessListener { result ->
                    cont.resume(result.text.lowercase())
                }
                .addOnFailureListener {
                    cont.resume("")
                }
        } catch (e: Exception) {
            cont.resume("")
        }
    }
}
