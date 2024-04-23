package com.rm.todocomposemvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.rm.todocomposemvvm.ui.TodoAppStart
import com.rm.todocomposemvvm.ui.theme.TodoComposeMvvmTheme
import com.rm.todocomposemvvm.ui.features.home.TodoTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TodoTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            TodoComposeMvvmTheme {
                TodoAppStart(viewModel)
            }
        }
    }
}