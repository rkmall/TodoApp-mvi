package com.rm.todocomposemvvm.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.domain.repository.TodoTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoTaskViewModel @Inject constructor(
    private val repository: TodoTaskRepository
) : ViewModel() {

    val searchAppbarState: MutableState<SearchAppbarState> = mutableStateOf(SearchAppbarState.CLOSED)

    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _allTasks = MutableStateFlow<TaskUiState<List<TodoTask>>>(TaskUiState.Loading)
    val allTasks: StateFlow<TaskUiState<List<TodoTask>>> = _allTasks.asStateFlow()

    private val _selectedTask: MutableStateFlow<TodoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<TodoTask?> = _selectedTask.asStateFlow()

    fun getAllTasks() {
        _allTasks.value = TaskUiState.Loading
        viewModelScope.launch {
            repository.getAllTodoTasks()
                .map { tasks ->
                    TaskUiState.Success(tasks) as TaskUiState<List<TodoTask>>
                }
                .catch { throwable ->
                    Log.d("todo:", "Thrown: $throwable")
                    _allTasks.value = TaskUiState.Error(throwable)
                }
                .collect { taskState ->
                _allTasks.value = taskState
            }
        }
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId)
                .collect { task ->
                    _selectedTask.value = task
                }
        }
    }
}

sealed class TaskUiState<out T> {
    data object Loading : TaskUiState<Nothing>()
    data class Success<T>(val data: T) : TaskUiState<T>()
    data class Error(val error: Throwable) : TaskUiState<Nothing>()
}

enum class SearchAppbarState {
    OPENED,
    CLOSED,
    TRIGGERED
}