package com.rm.todocomposemvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rm.todocomposemvvm.ui.screen.home.HomeContract
import com.rm.todocomposemvvm.ui.screen.home.HomeViewModel
import com.rm.todocomposemvvm.ui.screen.home.composables.HomeScreen
import com.rm.todocomposemvvm.ui.screen.task.TaskDetailContract
import com.rm.todocomposemvvm.ui.screen.task.TaskViewModel
import com.rm.todocomposemvvm.ui.screen.task.composables.TaskScreen
import com.rm.todocomposemvvm.ui.navigation.RouteArgument.HOME_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.navigation.RouteArgument.TASK_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route
    ) {
        homeRoute(
            onNavigateToTaskScreen = { taskId ->
                Route.Task.passTaskId(navController, taskId)
            }
        )

        taskRoute(
            onNavigateToHomeScreen = { snackBarMessage ->
                Route.Home.passSnackBarMessage(navController, snackBarMessage)
            }
        )
    }
}

private fun NavGraphBuilder.homeRoute(
    onNavigateToTaskScreen: (taskId: Int) -> Unit
) {
    composable(Route.Home.route) { backStackEntry ->
        val snackBarMessage = backStackEntry.savedStateHandle.get<String>(HOME_ROUTE_ARG_KEY)

        val viewModel = hiltViewModel<HomeViewModel>()

        HomeScreen(
            viewModel = viewModel,
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event ->  viewModel.setEvent(event) },
            snackBarMessage = snackBarMessage ?: EMPTY_STRING,
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is HomeContract.Effect.Navigation.ToTaskScreen) {
                    onNavigateToTaskScreen(navigationEffect.taskId)
                }
            }
        )
    }
}

private fun NavGraphBuilder.taskRoute(
    onNavigateToHomeScreen: (String) -> Unit
) {
    composable(
        route = Route.Task.route,
        arguments = listOf(navArgument(TASK_ROUTE_ARG_KEY) { type = NavType.IntType })
    ) { backStackEntry ->
        val argument = requireNotNull(backStackEntry.arguments) { "Task id required as an argument" }

        val viewModel = hiltViewModel<TaskViewModel, TaskViewModel.TaskViewModelFactory> { factory ->
            factory.create(argument.getInt(TASK_ROUTE_ARG_KEY))
        }

        TaskScreen(
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event -> viewModel.setEvent(event) },
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is TaskDetailContract.Effect.Navigation.ToHomeScreen) {
                    onNavigateToHomeScreen(navigationEffect.snackBarMessage)
                }
            }
        )
    }
}
