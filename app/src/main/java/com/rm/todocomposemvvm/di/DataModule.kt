package com.rm.todocomposemvvm.di

import android.content.Context
import com.rm.todocomposemvvm.data.repository.TodoTaskRepositoryImpl
import com.rm.todocomposemvvm.data.room.AppDatabase
import com.rm.todocomposemvvm.data.room.TodoTaskDao
import com.rm.todocomposemvvm.data.repository.TodoTaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideTodoTaskDao(database: AppDatabase): TodoTaskDao {
        return database.todoTaskDao()
    }

    @Singleton
    @Provides
    fun provideTodoTaskRepository(
        todoTaskDao: TodoTaskDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): TodoTaskRepository {
        return TodoTaskRepositoryImpl(todoTaskDao, dispatcher)
    }
}