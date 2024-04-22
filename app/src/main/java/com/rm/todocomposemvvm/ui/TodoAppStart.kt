package com.rm.todocomposemvvm.ui

import androidx.compose.runtime.Composable
import com.rm.todocomposemvvm.ui.navigation.AppNavGraph
import com.rm.todocomposemvvm.ui.viewmodel.TodoTaskViewModel

@Composable
fun TodoAppStart(
    viewModel: TodoTaskViewModel
) {

    AppNavGraph(viewModel)
}