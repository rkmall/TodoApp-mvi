package com.rm.todocomposemvvm.ui.features.task.composables

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.features.common.TaskOperation
import com.rm.todocomposemvvm.ui.components.PriorityItem
import com.rm.todocomposemvvm.ui.components.taskPriorityItemList
import com.rm.todocomposemvvm.ui.theme.PaddingExtraSmall
import com.rm.todocomposemvvm.ui.theme.PaddingMedium
import com.rm.todocomposemvvm.ui.theme.PaddingSmall
import com.rm.todocomposemvvm.ui.theme.PriorityDropDownHeight
import com.rm.todocomposemvvm.ui.theme.PriorityIndicatorSize
import com.rm.todocomposemvvm.ui.features.home.TodoTaskViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    viewModel: TodoTaskViewModel,
    selectedTask: TodoTask,
    navigateToListScreen: (TaskOperation) -> Unit
) {

    Scaffold(
        topBar = {
            Log.d("todo:", "Selected task screen: $selectedTask")
            TaskAppBar(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
        }
    ) { innerPaddings ->
        TaskContent(
            modifier = Modifier.padding(innerPaddings),
            title = selectedTask.title,
            onTitleChange = { userInput ->
                viewModel.title.value = userInput
            },
            description = selectedTask.description,
            onDescriptionChange = { userInput ->
                viewModel.description.value = userInput
            },
            priority = selectedTask.priority,
            onPrioritySelected = { priority ->
                viewModel.priority.value = priority
            }
        )
    }
}

@Composable
fun TaskContent(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = PaddingMedium, top = PaddingSmall, end = PaddingMedium, bottom = PaddingMedium)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title ,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title)) },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true
        )
        Spacer(modifier = Modifier.padding(PaddingExtraSmall))
        PriorityDropDownMenu(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        Spacer(modifier = Modifier.padding(PaddingExtraSmall))
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description ,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(R.string.description)) },
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun PriorityDropDownMenu(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = stringResource(R.string.prioritydropdownanimation)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(PriorityDropDownHeight)
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
            color = MaterialTheme.colorScheme.onSurface,

            )
        IconButton(
            modifier = Modifier
                .alpha(0.5f)
                .rotate(degrees = angle)
                .rotate(0f)
                .weight(1.5f),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Task Priority Drop Down")
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
