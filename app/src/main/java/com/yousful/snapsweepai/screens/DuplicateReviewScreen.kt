package com.yousful.snapsweepai.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.yousful.snapsweepai.data.DuplicateGroup
import com.yousful.snapsweepai.data.ScreenShotItem
import com.yousful.snapsweepai.logic.ImageDeleteUtil
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuplicateReviewScreen(
    title: String,
    duplicateGroups: List<DuplicateGroup>,
    onBack: () -> Unit,
    onDeleteConfirmed: (List<ScreenShotItem>) -> Unit
) {

    val context = LocalContext.current


    var isDeleting by remember { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<List<ScreenShotItem>>(emptyList()) }


    val deleteLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {


                ImageDeleteUtil.finalizeDelete(
                    context = context,
                    uris = pendingDelete.map { it.uri }
                )


                onDeleteConfirmed(pendingDelete)
            }

            pendingDelete = emptyList()
            isDeleting = false
        }


    val uniqueGroups = remember(duplicateGroups) {
        duplicateGroups.distinctBy { group ->
            (listOf(group.original) + group.duplicates)
                .map { it.id }
                .sorted()
                .joinToString("-")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
            // deep tech dark
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
                .padding(vertical = 18.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    enabled = !isDeleting,
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Make your phone cleaner with this sweep",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
            }
        }


        if (uniqueGroups.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No similar screenshots found 🎉",
                    color = Color(0xFFCBD5E1),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            return
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            items(uniqueGroups) { group ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0F172A)
                    ),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {


                        Text(
                            text = "AI Similar Group",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(14.dp))


                        Text(
                            text = "Kept (Best quality)",
                            color = Color(0xFF22C55E),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Image(
                            painter = rememberAsyncImagePainter(group.original.uri),
                            contentDescription = "Kept Screenshot",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        Text(
                            text = "Safe to delete",
                            color = Color(0xFFF87171),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(group.duplicates) { duplicate ->
                                Image(
                                    painter = rememberAsyncImagePainter(duplicate.uri),
                                    contentDescription = "Duplicate Screenshot",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            enabled = !isDeleting,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEF4444)
                            ),
                            onClick = {
                                isDeleting = true
                                pendingDelete = group.duplicates

                                ImageDeleteUtil.requestDelete(
                                    context = context,
                                    uris = group.duplicates.map { it.uri },
                                    launcher = deleteLauncher
                                )
                            }
                        ) {
                            if (isDeleting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Deleting…")
                            } else {
                                Text("Delete ${group.duplicates.size} screenshots")
                            }
                        }
                    }
                }
            }
        }
    }
}
