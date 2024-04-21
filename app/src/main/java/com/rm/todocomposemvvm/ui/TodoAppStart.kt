package com.rm.todocomposemvvm.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rm.todocomposemvvm.ui.navigation.AppNavGraph
import com.rm.todocomposemvvm.ui.navigation.AppNavigationActions
import com.rm.todocomposemvvm.ui.navigation.Route.LIST_ROUTE
import com.rm.todocomposemvvm.ui.viewmodel.TodoTaskViewModel

@Composable
fun TodoAppStart(
    viewModel: TodoTaskViewModel
) {
    val navController: NavHostController = rememberNavController()

    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }

    AppNavGraph(
        navController = navController,
        startDestination = LIST_ROUTE,
        appNavigationActions = navigationActions,
        viewModel = viewModel
    )
}