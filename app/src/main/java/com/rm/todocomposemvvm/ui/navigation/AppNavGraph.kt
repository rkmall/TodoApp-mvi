package com.rm.todocomposemvvm.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rm.todocomposemvvm.ui.features.home.HomeContract
import com.rm.todocomposemvvm.ui.features.home.HomeViewModel
import com.rm.todocomposemvvm.ui.features.home.composables.HomeScreen
import com.rm.todocomposemvvm.ui.features.task.TaskDetailContract
import com.rm.todocomposemvvm.ui.features.task.TaskViewModel
import com.rm.todocomposemvvm.ui.features.task.composables.TaskScreen
import com.rm.todocomposemvvm.ui.navigation.ScreenArgument.HOME_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.navigation.ScreenArgument.TASK_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING

@Composable
fun AppNavGraph(viewModel: HomeViewModel) {

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        homeRoute(
            viewModel = viewModel,
            onNavigateToTaskScreen = { taskId ->
                navController.navigate(Screen.Task.passTaskId(taskId))
            }
        )

        taskRoute(
            onNavigateToHomeScreen = {
                Screen.Home.passMessage(navController, it)
            }
        )
    }
}

private fun NavGraphBuilder.homeRoute(
    viewModel: HomeViewModel,
    onNavigateToTaskScreen: (taskId: Int) -> Unit
) {
    composable(
        route = Screen.Home.route
    ) {
        // Get the snackBarMessage passed from TaskScreen from SavedStateHandle
        val snackBarMessage = it.savedStateHandle.get<String>(HOME_ROUTE_ARG_KEY)

        HomeScreen(
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event ->  viewModel.setEvent(event) },
            snackBarMessage = snackBarMessage ?: EMPTY_STRING,
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is HomeContract.Effect.Navigation.ToTaskScreen) {
                    onNavigateToTaskScreen(navigationEffect.taskId) // pass taskId to TaskScreen
                }
            }
        )
    }
}

private fun NavGraphBuilder.taskRoute(
    onNavigateToHomeScreen: (String) -> Unit
) {
    composable(
        route = Screen.Task.route,
        arguments = listOf(navArgument(TASK_ROUTE_ARG_KEY) { type = NavType.IntType })
    ) {navBackStackEntry ->
        // Get taskId passed from HomeScreen from Bundle
        val argument = requireNotNull(navBackStackEntry.arguments) { "Task id required as an argument" }

        val viewModel = hiltViewModel<TaskViewModel, TaskViewModel.TaskViewModelFactory> { factory ->
            factory.create(argument.getInt(TASK_ROUTE_ARG_KEY))
        }

        TaskScreen(
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event -> viewModel.setEvent(event) },
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is TaskDetailContract.Effect.Navigation.ToHomeScreen) {
                    onNavigateToHomeScreen(navigationEffect.message) // pass snackBarMessage to HomeScreen
                }
            }
        )
    }
}
