package com.yousful.snapsweepai.logic

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest

object ImageDeleteUtil {


    fun requestDelete(
        context: Context,
        uris: List<Uri>,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        if (uris.isEmpty()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val deleteRequest = MediaStore.createDeleteRequest(
                context.contentResolver,
                uris
            )

            launcher.launch(
                IntentSenderRequest.Builder(deleteRequest.intentSender).build()
            )

        } else {

            uris.forEach { uri ->
                try {
                    context.contentResolver.delete(uri, null, null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun finalizeDelete(
        context: Context,
        uris: List<Uri>
    ) {
        uris.forEach { uri ->
            try {
                context.contentResolver.delete(uri, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
