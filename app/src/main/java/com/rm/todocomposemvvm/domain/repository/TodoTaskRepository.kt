package com.rm.todocomposemvvm.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import kotlinx.coroutines.flow.Flow

interface TodoTaskRepository {

    fun getAllTodoTasks(): Flow<List<TodoTask>>

    fun getSelectedTask(taskId: Int): Flow<TodoTask>

    suspend fun insertTodoTask(todoTask: TodoTask)

    suspend fun updateTodoTask(todoTask: TodoTask)

    suspend fun deleteTodoTask(todoTask: TodoTask)

    suspend fun deleteAllTodoTasks()

    fun searchTodoTask(searchQuery: String): Flow<List<TodoTask>>

    fun sortByLowPriority(): Flow<List<TodoTask>>

    fun sortByHighPriority(): Flow<List<TodoTask>>
}