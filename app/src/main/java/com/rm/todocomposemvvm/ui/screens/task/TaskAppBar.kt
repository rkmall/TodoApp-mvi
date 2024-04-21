package com.rm.todocomposemvvm.ui.screens.task

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.common.TaskOperation

@Composable
fun TaskAppBar(
    selectedTask: TodoTask?,
    navigateToListScreen: (TaskOperation) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: TodoTask,
    navigateToListScreen: (TaskOperation) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = selectedTask.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            BackAction(onBackClicked = { taskOp -> navigateToListScreen(taskOp) })
        },
        actions = {
            DeleteAction(navigateToListScreen)
            UpdateAction(navigateToListScreen)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(navigateToListScreen: (TaskOperation) -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.add_task))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            BackAction(onBackClicked = { taskOp -> navigateToListScreen(taskOp) })
        },
        actions = {
            AddAction(onAddClicked = { taskOp -> navigateToListScreen(taskOp) })
        }
    )
}

@Composable
fun BackAction(onBackClicked: (TaskOperation) -> Unit) {
    IconButton(onClick = { onBackClicked (TaskOperation.NO_ACTION) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.go_back),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AddAction(onAddClicked: (TaskOperation) -> Unit) {
    IconButton(onClick = { onAddClicked (TaskOperation.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_task),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: (TaskOperation) -> Unit
) {
    IconButton(onClick = { onDeleteClicked (TaskOperation.DELETE) }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_task),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (TaskOperation) -> Unit
) {
    IconButton(onClick = { onUpdateClicked (TaskOperation.UPDATE) }) {
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
    ExistingTaskAppBar(selectedTask = TodoTask(
        1,
        "Go Running",
        "Evening at 6 PM",
        Priority.MEDIUM)
    ) {}
    //TaskAppBar {}
}