package com.rm.todocomposemvvm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.ui.theme.PaddingMedium
import com.rm.todocomposemvvm.ui.theme.PriorityIndicatorSize

@Composable
fun PriorityItem(priority: Priority) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier.size(PriorityIndicatorSize)
        ) {
            drawCircle(color = priority.color)
        }
        Text(
            text = priority.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = PaddingMedium)
        )
    }
}

val taskPriorityItemList = listOf(
    Priority.LOW,
    Priority.MEDIUM,
    Priority.HIGH
)


val sortPriorityItemList = listOf(
    Priority.LOW,
    Priority.HIGH,
    Priority.NONE
)

@Preview
@Composable
fun PriorityItemPreview() {
    PriorityItem(Priority.HIGH)
}