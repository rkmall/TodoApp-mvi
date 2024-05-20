package com.rm.todocomposemvvm.ui.features.home

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import com.rm.todocomposemvvm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TodoTaskRepository
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>() {

    init {
        Log.d("message", "HomeViewModel called")
        getTodoTasks()
    }

    override fun setInitialState() = HomeContract.State(
        homeUiState = HomeUiState(emptyList(), AppBarUiState()),
        isLoading = true,
        isError = false
    )

    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.TaskItemClicked -> setEffect {
                HomeContract.Effect.Navigation.ToTaskScreen(taskId = event.taskId)
            }
            is HomeContract.Event.SearchTextInput -> setState {
                copy(homeUiState = homeUiState
                    .copy(appBarUiState = homeUiState.appBarUiState
                        .copy(searchText = event.searchQuery)
                    )
                )
            }
            else -> {}
        }
    }

    private fun getTodoTasks() {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            repository.getTodoTasks()
                .catch {
                    setState { copy(isError = true, isLoading = false) }
                }
                .collect { tasks ->
                    setState {
                        copy(homeUiState = homeUiState.copy(tasks = tasks), isLoading = false)
                    }
                    setEffect { HomeContract.Effect.DataWasLoaded }
                }
        }
    }
}