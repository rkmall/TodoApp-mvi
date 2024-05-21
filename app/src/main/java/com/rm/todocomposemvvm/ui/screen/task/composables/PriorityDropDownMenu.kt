package com.rm.todocomposemvvm.ui.screen.task.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.ui.component.PriorityItem
import com.rm.todocomposemvvm.ui.component.taskPriorityItemList
import com.rm.todocomposemvvm.ui.theme.PaddingMedium
import com.rm.todocomposemvvm.ui.theme.PriorityDropDownMenuHeight
import com.rm.todocomposemvvm.ui.theme.PriorityIndicatorSize

@Composable
fun PriorityDropDownMenu(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = stringResource(R.string.priority_dropdown_animation)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(PriorityDropDownMenuHeight)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                shape = MaterialTheme.shapes.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PriorityIndicatorSize)
                .weight(1f)
        ) {
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier
                .padding(start = PaddingMedium)
                .weight(8f),
            text = priority.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(
            modifier = Modifier
                .alpha(0.5f)
                .rotate(degrees = angle)
                .weight(1.5f),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Task Priority Drop Down"
            )
        }
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(0.938f),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            taskPriorityItemList.forEach { priority ->
                DropdownMenuItem(
                    text = { PriorityItem(priority = priority) },
                    onClick = {
                        expanded = false
                        onPrioritySelected(priority)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PriorityDropDownMenuPreview() {
    PriorityDropDownMenu(
        priority = Priority.LOW,
        onPrioritySelected =  {}
    )
}