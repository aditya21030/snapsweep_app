package com.yousful.snapsweepai.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.yousful.snapsweepai.viewmodel.ScreenshotViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotGridScreen(
    viewModel: ScreenshotViewModel
) {


    val screenshots by viewModel.screenshots.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val visualDuplicates by viewModel.visualDuplicateGroups.collectAsState()
    val isVisualScanning by viewModel.isVisualScanning.collectAsState()


    var showVisualDuplicateScreen by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Any?>(null) }
    var hasPermission by remember { mutableStateOf(false) }


    val permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }

    if (!hasPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Permission required to access screenshots")
        }
        return
    }



    if (showVisualDuplicateScreen) {

        when {
            isVisualScanning -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("Scanning screenshots…")

                    }
                }
            }

            else -> {
                DuplicateReviewScreen(
                    title = "Smart Deletion",
                    duplicateGroups = visualDuplicates,
                    onBack = { showVisualDuplicateScreen = false },
                    onDeleteConfirmed = { viewModel.onDeleteConfirmed(it) }
                )
            }
        }

    } else {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF020617)) // dark tech background
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF6366F1),
                                Color(0xFF22D3EE)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "SnapSweep Delete",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = "${screenshots.size} screenshots",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Button(
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        onClick = {
                            showVisualDuplicateScreen = true
                            viewModel.detectVisualDuplicates()
                        }
                    ) {
                        Text(
                            text = "Clean 🚀",
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }


            CategoryTabs(
                selected = selectedCategory,
                onSelect = { viewModel.selectCategory(it) }
            )


            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = screenshots,
                    key = { it.id }
                ) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                selectedImageUri = item.uri
                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.uri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }


    selectedImageUri?.let { uri ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable { selectedImageUri = null },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
