package com.rm.todocomposemvvm.ui.features.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.base.BaseViewModel
import com.rm.todocomposemvvm.ui.utils.isBlankOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TodoTaskRepository
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>() {

    private val _inputText: MutableStateFlow<String> = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private var job: Job? = null

    init {
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

            is HomeContract.Event.SearchTextInput -> {
                _inputText.update { event.searchQuery }
                onSearchTextChange(event.searchQuery)
            }

            else -> {}
        }
    }

    private fun onSearchTextChange(input: String) {
        if (input.isBlankOrEmpty()) {
            getTodoTasks()
        } else {
            getSearchedTasks()
        }
    }


    @OptIn(FlowPreview::class)
    private fun getSearchedTasks() {
        job?.cancel()
        job = viewModelScope.launch {
            inputText.debounce(1000).collectLatest { input ->
                if (input.isBlankOrEmpty()) {
                    return@collectLatest
                }

                repository.searchTodoTask("%$input%")
                    .collect { tasks ->
                        Log.d("search1", "$tasks")
                        setState {
                            copy(homeUiState = homeUiState.copy(tasks = tasks), isLoading = false)
                        }
                    }
            }
        }
    }

    private fun getTodoTasks() {
        setState { copy(isLoading = true, isError = false) }
        job?.cancel()
        job = viewModelScope.launch {
            repository.getTodoTasks()
                .catch {
                    setState { copy(isError = true, isLoading = false) }
                }
                .onCompletion {
                    Log.d("screen", "HomeViewModel flow completed")
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


