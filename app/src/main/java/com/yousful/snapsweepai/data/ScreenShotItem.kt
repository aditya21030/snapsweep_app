package com.yousful.snapsweepai.data

import android.net.Uri

data class ScreenShotItem(

    val id : Long,
    val uri : Uri,
    val name : String,
    val dateAdded : Long,
    val category: ScreenshotCategory,
    val extractedText : String? = null,
    val fileSize: Long,
    val hash: String? = null



)
