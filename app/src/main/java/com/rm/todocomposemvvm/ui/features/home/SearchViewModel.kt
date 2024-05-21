package com.rm.todocomposemvvm.ui.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TodoTaskRepository
) : ViewModel() {

    private val _inputText: MutableStateFlow<String> = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _tasks: MutableStateFlow<List<TodoTask>> = MutableStateFlow(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            inputText.debounce(500).collectLatest { input ->
                if (input.isBlank()) {
                    return@collectLatest
                }

                repository.searchTodoTask("%$input%")
                    .collect {
                        Log.d("search1", "$it")
                        _tasks.update { it }
                    }
            }
        }
    }

    fun onSearchTextChange(inputText: String) {
        _inputText.update { inputText }
    }

    private fun String.blankOrEmpty() = this.isBlank() || this.isEmpty()
}