package com.rm.todocomposemvvm.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoTaskDao {

    @Query("SELECT * FROM todo_task ORDER BY id ASC")
    fun getAllTodoTasks(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_task WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<TodoTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTask(todoTask: TodoTask)

    @Update
    suspend fun updateTodoTask(todoTask: TodoTask)

    @Delete
    suspend fun deleteTodoTask(todoTask: TodoTask)

    @Query("DELETE FROM todo_task")
    suspend fun deleteAllTodoTasks()

    @Query("SELECT * FROM todo_task WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchTodoTask(searchQuery: String): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_task ORDER BY CASE " +
            "WHEN priority LIKE 'L%' THEN 1 " +
            "WHEN priority LIKE 'M%' THEN 2 " +
            "WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_task ORDER BY CASE " +
            "WHEN priority LIKE 'H%' THEN 1 " +
            "WHEN priority LIKE 'M%' THEN 2 " +
            "WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<TodoTask>>
}
