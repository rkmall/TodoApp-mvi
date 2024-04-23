package com.rm.todocomposemvvm.ui.features.task

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (
    val repository: TodoTaskRepository
) : BaseViewModel<TaskDetailContract.State, TaskDetailContract.Event, TaskDetailContract.Effect>() {

    override fun setInitialState() = TaskDetailContract.State(
        task = TodoTask(),
        isLoading = true,
        isError = false
    )

    init {
        setEffect { TaskDetailContract.Effect.Navigation.FromListScreen }
    }

    override fun handleEvents(event: TaskDetailContract.Event) {
        when (event) {
            is TaskDetailContract.Event.AddIconClicked -> insertTask(event.task)

            is TaskDetailContract.Event.UpdateIconClicked ->  updateTask(event.task)

            is TaskDetailContract.Event.DeleteIconClicked -> deleteTask(event.task)

            is TaskDetailContract.Event.BackButtonClicked -> setEffect {
                TaskDetailContract.Effect.Navigation.ToHomeScreen
            }

            is TaskDetailContract.Event.TitleTextInput -> setState {
                copy(task = task?.copy(title = event.title))
            }

            is TaskDetailContract.Event.DescriptionTextInput -> setState {
                copy(task = task?.copy(description = event.description))
            }

            is TaskDetailContract.Event.PrioritySelection -> setState {
                copy(task = task?.copy(priority = event.priority))
            }
        }
    }

    fun getSelectedTask(taskId: Int) {
        Log.d("todo:", "getSelectedTask called")
        if (taskId > -1) {
            viewModelScope.launch {
                setState { copy(isLoading = true, isError = false) }

                repository.getSelectedTask(taskId)
                    .catch {
                        setState { copy(isLoading = false, isError = true) }
                    }
                    .collect { task ->
                        setState { copy(task = task, isLoading = false, isError = false ) }
                    }
            }
        } else {
            setState { copy(isLoading = false, isError = false) }
        }
    }

    private fun insertTask(task: TodoTask) {
        viewModelScope.launch {
            repository.insertTodoTask(task)
        }
    }

    private fun updateTask(task: TodoTask) {
        viewModelScope.launch {
            repository.updateTodoTask(task)
        }
    }

    private fun deleteTask(task: TodoTask) {
        viewModelScope.launch {
            repository.deleteTodoTask(task)
        }
    }
}