package com.rm.todocomposemvvm.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rm.todocomposemvvm.ui.features.home.HomeContract
import com.rm.todocomposemvvm.ui.features.home.HomeViewModel
import com.rm.todocomposemvvm.ui.features.home.SearchViewModel
import com.rm.todocomposemvvm.ui.features.home.composables.HomeScreen
import com.rm.todocomposemvvm.ui.features.task.TaskDetailContract
import com.rm.todocomposemvvm.ui.features.task.TaskViewModel
import com.rm.todocomposemvvm.ui.features.task.composables.TaskScreen
import com.rm.todocomposemvvm.ui.navigation.ScreenArgument.HOME_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.navigation.ScreenArgument.TASK_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route
    ) {
        homeRoute(
            onNavigateToTaskScreen = { taskId ->
                navController.navigate(Route.Task.passTaskId(taskId))
            }
        )

        taskRoute(
            onNavigateToHomeScreen = {
                Route.Home.passMessage(navController, it)
            }
        )
    }
}

private fun NavGraphBuilder.homeRoute(
    onNavigateToTaskScreen: (taskId: Int) -> Unit
) {
    composable(Route.Home.route) {
        val snackBarMessage = it.savedStateHandle.get<String>(HOME_ROUTE_ARG_KEY)

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
    ) {
        val argument = requireNotNull(it.arguments) { "Task id required as an argument" }

        val viewModel = hiltViewModel<TaskViewModel, TaskViewModel.TaskViewModelFactory> { factory ->
            factory.create(argument.getInt(TASK_ROUTE_ARG_KEY))
        }

        TaskScreen(
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event -> viewModel.setEvent(event) },
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is TaskDetailContract.Effect.Navigation.ToHomeScreen) {
                    onNavigateToHomeScreen(navigationEffect.message)
                }
            }
        )
    }
}
