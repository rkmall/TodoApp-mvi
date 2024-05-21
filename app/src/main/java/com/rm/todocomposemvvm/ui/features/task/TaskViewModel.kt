package com.rm.todocomposemvvm.ui.features.task

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.base.BaseViewModel
import com.rm.todocomposemvvm.ui.utils.AppConstants.DEFAULT_TASK_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TaskViewModel.TaskViewModelFactory::class)
class TaskViewModel @AssistedInject constructor (
    @Assisted val taskId: Int,
    val repository: TodoTaskRepository
) : BaseViewModel<TaskDetailContract.State, TaskDetailContract.Event, TaskDetailContract.Effect>() {

    override fun setInitialState() = TaskDetailContract.State(
        task = TodoTask(),
        isLoading = true,
        isError = false
    )

    init {
        getSelectedTask()
    }

    override fun handleEvents(event: TaskDetailContract.Event) {
        when (event) {
            is TaskDetailContract.Event.AddIconClicked -> {
                if (validateFields(event.task.title, event.task.description)) {
                    insertTask(event.task)
                } else {
                    setEffect {
                        TaskDetailContract.Effect.ShowSnackBar("Please enter the your task details")
                    }
                }
            }

            is TaskDetailContract.Event.UpdateIconClicked -> {
                updateTask(event.task)
            }

            is TaskDetailContract.Event.DeleteIconClicked -> {
                deleteTask(event.task)
            }

            is TaskDetailContract.Event.BackIconClicked -> setEffect {
                TaskDetailContract.Effect.Navigation.ToHomeScreen(event.message)
            }

            is TaskDetailContract.Event.TitleTextInput -> updateTitle(event.title)

            is TaskDetailContract.Event.DescriptionTextInput -> setState {
                copy(task = task?.copy(description = event.description))
            }

            is TaskDetailContract.Event.PrioritySelection -> setState {
                copy(task = task?.copy(priority = event.priority))
            }
        }
    }

    private fun getSelectedTask() {
        if (taskId > DEFAULT_TASK_ID) {
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

    private fun updateTitle(title: String) {
        if (title.length < 30) {
            setState {
                copy(task = task?.copy(title = title))
            }
        }
    }

    private fun validateFields(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
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

    @AssistedFactory
    interface TaskViewModelFactory {
        fun create(taskId: Int): TaskViewModel
    }
}