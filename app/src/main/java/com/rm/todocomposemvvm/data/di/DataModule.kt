package com.rm.todocomposemvvm.data.di

import android.content.Context
import com.rm.todocomposemvvm.data.repository.TodoTaskRepositoryImpl
import com.rm.todocomposemvvm.data.room.AppDatabase
import com.rm.todocomposemvvm.data.room.TodoTaskDao
import com.rm.todocomposemvvm.domain.repository.TodoTaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideTodoTaskDao(database: AppDatabase): TodoTaskDao = database.todoTaskDao()

    @Singleton
    @Provides
    fun provideTodoTaskRepository(todoTaskDao: TodoTaskDao): TodoTaskRepository {
        return TodoTaskRepositoryImpl(todoTaskDao)
    }
}