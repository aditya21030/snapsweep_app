package com.yousful.snapsweepai.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.yousful.snapsweepai.data.ScreenShotItem
import com.yousful.snapsweepai.logic.ScreenshotClassifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScreenshotRepository(private val context: Context) {

    suspend fun getScreenshots(): List<ScreenShotItem> =
        withContext(Dispatchers.IO) {

            val screenshots = mutableListOf<ScreenShotItem>()
            val resolver: ContentResolver = context.contentResolver


            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.SIZE
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->

                val idCol =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameCol =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dateCol =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val pathCol =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
                val sizeCol =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                while (cursor.moveToNext()) {

                    val id = cursor.getLong(idCol)
                    val name = cursor.getString(nameCol)
                    val date = cursor.getLong(dateCol)
                    val relativePath = cursor.getString(pathCol)
                    val fileSize = cursor.getLong(sizeCol) // 🔥 READ FILE SIZE


                    if (!ScreenshotClassifier.isScreenshot(name, relativePath)) {
                        continue
                    }


                    val category =
                        ScreenshotClassifier.classifyScreenshot(name)

                    val uri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )

                    screenshots.add(
                        ScreenShotItem(
                            id = id,
                            uri = uri,
                            name = name,
                            dateAdded = date,
                            fileSize = fileSize,
                            category = category
                        )
                    )
                }
            }

            screenshots
        }
}
