package com.rm.todocomposemvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rm.todocomposemvvm.ui.navigation.AppNavGraph
import com.rm.todocomposemvvm.ui.theme.TodoComposeMvvmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoComposeMvvmTheme {
                val navController: NavHostController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}