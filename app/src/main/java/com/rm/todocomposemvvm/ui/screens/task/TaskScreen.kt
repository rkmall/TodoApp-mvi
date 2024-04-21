package com.rm.todocomposemvvm.ui.screens.task

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.common.TaskOperation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    selectedTask: TodoTask?,
    navigateToListScreen: (TaskOperation) -> Unit
) {

    Scaffold(
        topBar = {
            TaskAppBar(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
        }
    ) {}
}


@Preview
@Composable
fun TaskScreenPreview() {
    TaskScreen(
        selectedTask = null,
        navigateToListScreen = {}
    )
}

