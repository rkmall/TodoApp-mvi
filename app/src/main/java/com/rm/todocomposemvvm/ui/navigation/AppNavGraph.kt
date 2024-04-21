package com.rm.todocomposemvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rm.todocomposemvvm.ui.common.AppConstants
import com.rm.todocomposemvvm.ui.common.TaskOperation
import com.rm.todocomposemvvm.ui.screens.list.ListScreen
import com.rm.todocomposemvvm.ui.screens.task.TaskScreen
import com.rm.todocomposemvvm.ui.viewmodel.TodoTaskViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.LIST_ROUTE,
    appNavigationActions: AppNavigationActions,
    viewModel: TodoTaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        listComposable(
            viewModel = viewModel,
            navigateToTaskScreen = appNavigationActions.navigateToTaskScreen
        )

        taskComposable(
            viewModel = viewModel,
            navigateToListScreen = appNavigationActions.navigateToListScreen
        )
    }
}

fun NavGraphBuilder.listComposable(
    viewModel: TodoTaskViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit = {}
) {
    composable(
        route = Route.LIST_ROUTE,
        arguments = listOf(navArgument(AppConstants.LIST_SCREEN_ARG_KEY) {
            type = NavType.StringType
        })
    ) {
        ListScreen(
            viewModel = viewModel,
            navigateToTaskScreen =  navigateToTaskScreen
        )
    }
}

fun NavGraphBuilder.taskComposable(
    viewModel: TodoTaskViewModel,
    navigateToListScreen: (TaskOperation) -> Unit = {}
) {
    composable(
        route = Route.TASK_ROUTE,
        arguments = listOf(
            navArgument(AppConstants.TASK_SCREEN_ARG_KEY) { type = NavType.IntType }
        )
    ) {navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(AppConstants.TASK_SCREEN_ARG_KEY)
        viewModel.getSelectedTask(taskId)
        val selectedTask by viewModel.selectedTask.collectAsState()

        TaskScreen(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
    }
}