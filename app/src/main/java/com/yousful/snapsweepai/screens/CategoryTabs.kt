package com.yousful.snapsweepai.screens


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yousful.snapsweepai.data.ScreenshotCategory

@Composable
fun CategoryTabs(
    selected: ScreenshotCategory,
    onSelect: (ScreenshotCategory) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        ScreenshotCategory.values().forEach { category ->
            FilterChip(
                selected = category == selected,
                onClick = { onSelect(category) },
                label = {
                    Text(
                        text = category.name.replace("_", " ")
                    )
                },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
