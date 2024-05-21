package com.rm.todocomposemvvm.ui.screen.home

import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING

data class HomeUiState(
    val tasks: List<TodoTask>,
    val appBarUiState: AppBarUiState
)

data class AppBarUiState(
    var searchText: String = EMPTY_STRING
)