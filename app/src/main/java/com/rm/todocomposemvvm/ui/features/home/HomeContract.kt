package com.rm.todocomposemvvm.ui.features.home

import com.rm.todocomposemvvm.ui.base.ViewEvent
import com.rm.todocomposemvvm.ui.base.ViewSideEffect
import com.rm.todocomposemvvm.ui.base.ViewState

class HomeContract {

    sealed class Event : ViewEvent {
        data object SortIconClicked: Event()
        data object DeleteAllIconClicked : Event()
        data class SearchClicked(val searchQuery: String) : Event()
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

        sealed class Navigation : Effect() {
            data class ToTaskScreen(val taskId: Int) : Navigation()
        }
    }
}