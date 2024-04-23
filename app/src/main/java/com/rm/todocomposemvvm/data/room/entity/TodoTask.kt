package com.rm.todocomposemvvm.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rm.todocomposemvvm.ui.utils.AppConstants.DEFAULT_TASK_ID
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING

@Entity(tableName = "todo_task")
data class TodoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_TASK_ID,
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val priority: Priority = Priority.LOW
)
