package com.rm.todocomposemvvm.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
    val searchTextState: MutableState<String> = mutableStateOf(EMPTY_STRING)

    val id: MutableState<Int> = mutableIntStateOf(0)
    val title: MutableState<String> = mutableStateOf(EMPTY_STRING)
    val description: MutableState<String> = mutableStateOf(EMPTY_STRING)
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    private val _allTasks = MutableStateFlow<TaskUiState<List<TodoTask>>>(TaskUiState.Loading)
    val allTasks: StateFlow<TaskUiState<List<TodoTask>>> = _allTasks.asStateFlow()

    private val _selectedTask: MutableStateFlow<TodoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<TodoTask?> = _selectedTask.asStateFlow()

    private val _clickedTask: MutableStateFlow<TodoTask> = MutableStateFlow(TodoTask())
    val clickedTask: StateFlow<TodoTask> = _clickedTask.asStateFlow()

    init {
        getAllTasks()
    }

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

    fun getClickedTask(index: Int) {
        _clickedTask.value = (allTasks.value as TaskUiState.Success).data[index]
    }

    fun setClickedTask() {
        _clickedTask.value = TodoTask()
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId)
                .collect { task ->
                    Log.d("todo:", "$task")
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