package com.rm.todocomposemvvm.data.repository

import com.rm.todocomposemvvm.data.room.entity.TodoTask
import kotlinx.coroutines.flow.Flow

interface TodoTaskRepository {

    fun getTodoTasks(): Flow<List<TodoTask>>

    fun getSelectedTask(taskId: Int): Flow<TodoTask>

    suspend fun insertTodoTask(todoTask: TodoTask)

    suspend fun updateTodoTask(todoTask: TodoTask)

    suspend fun deleteTodoTask(todoTask: TodoTask)

    suspend fun deleteAllTodoTasks()

    fun searchTodoTask(searchQuery: String): Flow<List<TodoTask>>

    fun sortByLowPriority(): Flow<List<TodoTask>>

    fun sortByHighPriority(): Flow<List<TodoTask>>
}