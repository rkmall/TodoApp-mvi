package com.rm.todocomposemvvm.data.repository

import com.rm.todocomposemvvm.data.room.entity.TodoTask
import kotlinx.coroutines.flow.Flow

interface TodoTaskRepository {

    suspend fun getTodoTasks(): Flow<List<TodoTask>>

    suspend fun getSelectedTask(taskId: Int): Flow<TodoTask>

    suspend fun insertTodoTask(todoTask: TodoTask)

    suspend fun updateTodoTask(todoTask: TodoTask)

    suspend fun deleteTodoTask(todoTask: TodoTask)

    suspend fun deleteAllTodoTasks()

    suspend fun searchTodoTask(searchQuery: String): Flow<List<TodoTask>>

    suspend fun sortByLowPriority(): Flow<List<TodoTask>>

    suspend fun sortByHighPriority(): Flow<List<TodoTask>>
}