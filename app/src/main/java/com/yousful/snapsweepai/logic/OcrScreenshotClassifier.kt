package com.yousful.snapsweepai.logic

import com.yousful.snapsweepai.data.ScreenshotCategory

object OcrScreenshotClassifier {

    fun classify(text: String): ScreenshotCategory {

        return when {

            // PAYMENTS
            text.contains("upi")
                    || text.contains("gpay")
                    || text.contains("paytm")
                    || text.contains("phonepe")
                    || text.contains("transaction")
                    || text.contains("payment") ->
                ScreenshotCategory.PAYMENTS

            //  CHATS
            text.contains("message")
                    || text.contains("sent")
                    || text.contains("received")
                    || text.contains("typing")
                    || text.contains("online")
                    || text.contains("last seen")
                    || text.contains("whatsapp")
                    || text.contains("telegram")
                    || text.contains("instagram")
                    || text.contains("chat") ->
                ScreenshotCategory.CHATS

            // ORDERS
            text.contains("order")
                    || text.contains("delivered")
                    || text.contains("shipment")
                    || text.contains("tracking")
                    || text.contains("placed")
                    || text.contains("invoice") ->
                ScreenshotCategory.ORDERS

            // (settings, dashboards, app UI)
            text.contains("settings")
                    || text.contains("profile")
                    || text.contains("dashboard")
                    || text.contains("account")
                    || text.contains("privacy")
                    || text.contains("permission") ->
                ScreenshotCategory.APP_SCREENS

            else ->
                ScreenshotCategory.SCREENSHOTS
        }
    }
}
