package com.rm.todocomposemvvm.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_task")
data class TodoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
)
