package com.rm.todocomposemvvm

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.OnApplyWindowInsetsListener
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import com.rm.todocomposemvvm.ui.features.home.HomeViewModel
import com.rm.todocomposemvvm.ui.navigation.AppNavGraph
import com.rm.todocomposemvvm.ui.theme.TodoComposeMvvmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoComposeMvvmTheme {
                AppNavGraph(viewModel)
            }
        }
    }
}