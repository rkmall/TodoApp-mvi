package com.rm.todocomposemvvm.ui.features.task.composables

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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.TodoTask

@Composable
fun TaskAppBar(
    selectedTask: TodoTask,
    onAddClicked: (selectedTask: TodoTask) -> Unit,
    onUpdateClicked: (selectedTask: TodoTask) -> Unit,
    onDeleteClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: () -> Unit,
) {
    ExistingTaskAppBar(
        selectedTask = selectedTask,
        onUpdateClicked = onUpdateClicked,
        onDeleteClicked = onDeleteClicked,
        onAddClicked = onAddClicked,
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: TodoTask,
    onUpdateClicked: (selectedTask: TodoTask) -> Unit,
    onDeleteClicked: (selectedTask: TodoTask) -> Unit,
    onAddClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = {
            if (selectedTask.id == 0) {
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
        navigationIcon = {
            BackAction(onBackClicked = onBackClicked)
        },
        actions = {
            if (selectedTask.id == 0) {
                AddAction(
                    selectedTask = selectedTask,
                    onAddClicked = { onAddClicked(it) },
                    onBackClicked = onBackClicked
                )
            } else {
                DeleteAction(
                    selectedTask = selectedTask,
                    onDeleteClicked = onDeleteClicked,
                    onBackClicked = onBackClicked
                )
                UpdateAction(
                    selectedTask = selectedTask,
                    onUpdateClicked = onUpdateClicked
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    selectedTask: TodoTask,
    onAddClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.add_task))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            BackAction(onBackClicked = onBackClicked)
        },
        actions = {
            AddAction(
                selectedTask = selectedTask,
                onAddClicked = onAddClicked,
                onBackClicked = onBackClicked
            )
        }
    )
}

@Composable
fun BackAction(onBackClicked: () -> Unit) {
    IconButton(onClick = { onBackClicked() }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.go_back),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AddAction(
    selectedTask: TodoTask,
    onAddClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: () -> Unit
) {
    IconButton(
        onClick = {
            onAddClicked(selectedTask)
            onBackClicked()
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
fun DeleteAction(
    selectedTask: TodoTask,
    onDeleteClicked: (selectedTask: TodoTask) -> Unit,
    onBackClicked: () -> Unit
) {
    IconButton(
        onClick = {
            onDeleteClicked(selectedTask)
            onBackClicked()
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
fun UpdateAction(
    selectedTask: TodoTask,
    onUpdateClicked: (selectedTask: TodoTask) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
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
    //TaskAppBar {}
}