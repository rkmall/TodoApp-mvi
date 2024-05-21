package com.rm.todocomposemvvm.ui.screen.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import com.rm.todocomposemvvm.ui.base.BaseViewModel
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TodoTaskRepository
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>() {

    private val _searchText: MutableStateFlow<String> = MutableStateFlow(EMPTY_STRING)
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _searchActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchActive: StateFlow<Boolean> = _searchActive.asStateFlow()

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

            is HomeContract.Event.SearchIconClicked -> {
                onSearchActiveStateChange(event.searchActive)
            }

            is HomeContract.Event.CloseIconClicked -> {
                onSearchActiveStateChange(event.searchActive)
            }

            is HomeContract.Event.SearchTextInput -> {
                _searchText.update { event.searchQuery }
                onSearchTextChange(event.searchQuery)
            }

            is HomeContract.Event.DeleteAllIconClicked -> {
                deleteAllTasks()
            }

            is HomeContract.Event.SortIconClicked -> {

            }

            else -> {}
        }
    }

    private fun onSearchActiveStateChange(searchActiveState: Boolean) {
        _searchActive.update { searchActiveState }
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
            searchText.debounce(1000).collectLatest { input ->
                if (input.isBlankOrEmpty()) {
                    return@collectLatest
                }

                repository.searchTodoTask("%$input%")
                    .catch {
                        setState { copy(isError = true, isLoading = false) }
                    }
                    .collect { tasks ->
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
                .collect { tasks ->
                    setState {
                        copy(homeUiState = homeUiState.copy(tasks = tasks), isLoading = false)
                    }
                    setEffect { HomeContract.Effect.DataWasLoaded }
                }
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch {
            repository.deleteAllTodoTasks()
        }
    }
}


