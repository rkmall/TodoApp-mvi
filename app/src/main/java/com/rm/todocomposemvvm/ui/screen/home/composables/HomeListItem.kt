package com.rm.todocomposemvvm.ui.screen.home.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rm.todocomposemvvm.ui.theme.PaddingLarge
import com.rm.todocomposemvvm.ui.theme.PriorityIndicatorSize
import com.rm.todocomposemvvm.ui.theme.RowItemElevation
import com.rm.todocomposemvvm.ui.theme.rowItemRowBackGroundColor
import com.rm.todocomposemvvm.ui.theme.rowItemTextColor

@Composable
fun HomeListItem(
    id: Int,
    title: String,
    description: String,
    color: Color,
    onNavigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.rowItemRowBackGroundColor,
        shape = RectangleShape,
        tonalElevation = RowItemElevation,
        onClick = { onNavigateToTaskScreen(id) }
    ) {
        Column(
            modifier = Modifier
                .padding(PaddingLarge),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .weight(8f),
                    text = title,
                    color = MaterialTheme.colorScheme.rowItemTextColor,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.CenterEnd

                ) {
                    Canvas(
                        modifier = Modifier.size(PriorityIndicatorSize)
                    ) {
                        drawCircle(color = color)
                    }
                }
            }
            Text(
                text = description,
                color = MaterialTheme.colorScheme.rowItemTextColor,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}
