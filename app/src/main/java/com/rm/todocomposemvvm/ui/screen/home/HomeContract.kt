package com.rm.todocomposemvvm.ui.screen.home

import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.base.ViewEvent
import com.rm.todocomposemvvm.ui.base.ViewSideEffect
import com.rm.todocomposemvvm.ui.base.ViewState
import com.rm.todocomposemvvm.ui.screen.task.TaskDetailContract

class HomeContract {

    sealed class Event : ViewEvent {
        data object DeleteIconClicked : Event()
        data object ConfirmDeletion : Event()
        data class SortIconClicked(val priority: Priority): Event()
        data class SearchIconClicked(val searchActive: Boolean) : Event()
        data class CloseIconClicked(val searchActive: Boolean) : Event()
        data class SearchTextInput(val searchQuery: String) : Event()
        data class TaskItemClicked(val taskId: Int ) : Event()
    }

    data class State(
        val homeUiState: HomeUiState,
        val isLoading: Boolean,
        val isError: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object DataWasLoaded : Effect()

        data object ShowAlertDialog : Effect()

        sealed class Navigation : Effect() {
            data class ToTaskScreen(val taskId: Int) : Navigation()
        }
    }
}