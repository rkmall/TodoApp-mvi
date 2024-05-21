package com.rm.todocomposemvvm.data.repository

import android.util.Log
import com.rm.todocomposemvvm.data.room.TodoTaskDao
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoTaskRepositoryImpl @Inject constructor(private val todoTaskDao: TodoTaskDao) : TodoTaskRepository {

    override fun getTodoTasks(): Flow<List<TodoTask>> {
        Log.d("todo:", "FETCHING ALL TASKS")
        return todoTaskDao.getAllTodoTasks()
    }

    override fun getSelectedTask(taskId: Int): Flow<TodoTask> {
        Log.d("todo:", "FETCHING SELECTED TASK")
        return todoTaskDao.getSelectedTask(taskId)
    }

    override suspend fun insertTodoTask(todoTask: TodoTask) {
        Log.d("todo:", "INSERTING TASK")
        return todoTaskDao.insertTodoTask(todoTask)
    }

    override suspend fun updateTodoTask(todoTask: TodoTask) {
        Log.d("todo:", "UPDATING TASK")
        return todoTaskDao.updateTodoTask(todoTask)
    }

    override suspend fun deleteTodoTask(todoTask: TodoTask) {
        Log.d("todo:", "DELETING TASK")
        return todoTaskDao.deleteTodoTask(todoTask)
    }

    override suspend fun deleteAllTodoTasks() {
        Log.d("todo:", "DELETING ALL TASKS")
        todoTaskDao.deleteAllTodoTasks()
    }

    override fun searchTodoTask(searchQuery: String): Flow<List<TodoTask>> {
        Log.d("todo:", "SEARCHING TASKS")
        return todoTaskDao.searchTodoTask(searchQuery)
    }

    override fun sortByLowPriority(): Flow<List<TodoTask>> {
        Log.d("todo:", "SORTING TASKS BY LOW PRIORITY")
        return todoTaskDao.sortByLowPriority()
    }

    override fun sortByHighPriority(): Flow<List<TodoTask>> {
        Log.d("todo:", "SORTING TASKS BY HIGH PRIORITY")
        return todoTaskDao.sortByHighPriority()
    }
}
