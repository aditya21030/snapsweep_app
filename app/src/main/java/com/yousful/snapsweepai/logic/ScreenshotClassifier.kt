package com.yousful.snapsweepai.logic

import com.yousful.snapsweepai.data.ScreenshotCategory

object ScreenshotClassifier {


    fun isScreenshot(
        fileName: String,
        relativePath: String?
    ): Boolean {

        val name = fileName.lowercase()
        val path = relativePath?.lowercase() ?: ""

        return (
                name.startsWith("screenshot") ||
                        name.contains("screenshot") ||
                        path.contains("screenshots")
                )
    }


    fun classifyScreenshot(fileName: String): ScreenshotCategory {

        val name = fileName.lowercase()

        return when {

            name.contains("whatsapp") || name.contains("wa") ->
                ScreenshotCategory.WHATSAPP

            name.contains("upi")
                    || name.contains("gpay")
                    || name.contains("paytm")
                    || name.contains("phonepe")
                    || name.contains("payment") ->
                ScreenshotCategory.PAYMENTS

            else ->
                ScreenshotCategory.SCREENSHOTS
        }
    }
}
