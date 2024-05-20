package com.rm.todocomposemvvm.ui.features.task.composables

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.utils.AppConstants.DEFAULT_TASK_ID
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAppBar(
    selectedTask: TodoTask,
    onAddClicked: (selectedTask: TodoTask) -> Unit,
    onUpdateClicked: (selectedTask: TodoTask) -> Unit,
    onDeleteClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: (String) -> Unit,
) {
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    TopAppBar(
        title = {
            if (selectedTask.id == DEFAULT_TASK_ID) {
                Text(text = stringResource(id = R.string.add_task))
            } else {
                Text(
                    text = selectedTask.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = { BackActionIcon(onBackClicked = { onBackClicked(EMPTY_STRING) }) },
        actions = {
            if (selectedTask.id == DEFAULT_TASK_ID) {
                AddActionIcon(
                    selectedTask = selectedTask,
                    onAddClicked = onAddClicked,
                    onBackClicked = onBackClicked
                )
            } else {
                DeleteActionIcon(
                    selectedTask = selectedTask,
                    onDeleteClicked = onDeleteClicked,
                    onBackClicked = onBackClicked
                )
                UpdateActionIcon(
                    selectedTask = selectedTask,
                    keyboardController = keyboardController,
                    onUpdateClicked = onUpdateClicked
                )
            }
        }
    )
}

@Composable
fun BackActionIcon(onBackClicked: () -> Unit) {
    IconButton(onClick = { onBackClicked() }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.go_back),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AddActionIcon(
    selectedTask: TodoTask,
    onAddClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: (String) -> Unit
) {
    IconButton(
        onClick = {
            onAddClicked(selectedTask)
            onBackClicked("Added: ${selectedTask.title}")
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_task),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DeleteActionIcon(
    selectedTask: TodoTask,
    onDeleteClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: (String) -> Unit
) {
    IconButton(
        onClick = {
            onDeleteClicked(selectedTask)
            onBackClicked("Deleted: ${selectedTask.title}")
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_task),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun UpdateActionIcon(
    selectedTask: TodoTask,
    keyboardController: SoftwareKeyboardController?,
    onUpdateClicked: (selectedTask: TodoTask) -> Unit
) {
    IconButton(
        onClick = {
            keyboardController?.hide()
            onUpdateClicked(selectedTask)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.update_task),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun TaskAppBarPreview() {
    TaskAppBar(
        selectedTask = TodoTask(1, "Go running", "Running at 6 PM", Priority.LOW),
        onAddClicked = {},
        onUpdateClicked = {},
        onDeleteClicked = {},
        onBackClicked = {}
    )
}