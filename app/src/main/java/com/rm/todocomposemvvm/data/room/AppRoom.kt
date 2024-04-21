package com.rm.todocomposemvvm.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rm.todocomposemvvm.data.room.entity.TodoTask


@Database(entities = [TodoTask::class], version = 1, exportSchema = false )
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoTaskDao() : TodoTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}