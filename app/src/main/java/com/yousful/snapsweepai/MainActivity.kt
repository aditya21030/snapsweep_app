package com.yousful.snapsweepai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.yousful.snapsweepai.screens.ScreenshotGridScreen
import com.yousful.snapsweepai.ui.theme.SnapSweepAITheme
import com.yousful.snapsweepai.viewmodel.ScreenshotViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            SnapSweepAITheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {

                    val viewModel: ScreenshotViewModel =
                        androidx.lifecycle.viewmodel.compose.viewModel()

                    ScreenshotGridScreen(viewModel = viewModel)
                }
            }
        }
    }
}

