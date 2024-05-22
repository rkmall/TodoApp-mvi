package com.rm.todocomposemvvm.data.repository

import android.util.Log
import com.rm.todocomposemvvm.data.room.TodoTaskDao
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoTaskRepositoryImpl @Inject constructor(
    private val todoTaskDao: TodoTaskDao,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : TodoTaskRepository {

    override suspend fun getTodoTasks(): Flow<List<TodoTask>> = withContext(dispatcher) {
        Log.d("todo:", "FETCHING ALL TASKS")
        todoTaskDao.getAllTodoTasks()
    }

    override suspend fun getSelectedTask(taskId: Int): Flow<TodoTask> = withContext(dispatcher) {
        Log.d("todo:", "FETCHING SELECTED TASK")
        todoTaskDao.getSelectedTask(taskId)
    }

    override suspend fun insertTodoTask(todoTask: TodoTask) = withContext(dispatcher) {
        Log.d("todo:", "INSERTING TASK")
        todoTaskDao.insertTodoTask(todoTask)
    }

    override suspend fun updateTodoTask(todoTask: TodoTask) = withContext(dispatcher) {
        Log.d("todo:", "UPDATING TASK")
        todoTaskDao.updateTodoTask(todoTask)
    }

    override suspend fun deleteTodoTask(todoTask: TodoTask) = withContext(dispatcher) {
        Log.d("todo:", "DELETING TASK")
        todoTaskDao.deleteTodoTask(todoTask)
    }

    override suspend fun deleteAllTodoTasks() = withContext(dispatcher) {
        Log.d("todo:", "DELETING ALL TASKS")
        todoTaskDao.deleteAllTodoTasks()
    }

    override suspend fun searchTodoTask(searchQuery: String): Flow<List<TodoTask>> = withContext(dispatcher) {
        Log.d("todo:", "SEARCHING TASKS")
        todoTaskDao.searchTodoTask(searchQuery)
    }

    override suspend fun sortByLowPriority(): Flow<List<TodoTask>> = withContext(dispatcher) {
        Log.d("todo:", "SORTING TASKS BY LOW PRIORITY")
        todoTaskDao.sortByLowPriority()
    }

    override suspend fun sortByHighPriority(): Flow<List<TodoTask>> = withContext(dispatcher) {
        Log.d("todo:", "SORTING TASKS BY HIGH PRIORITY")
        todoTaskDao.sortByHighPriority()
    }
}
