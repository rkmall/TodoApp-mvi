package com.rm.todocomposemvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rm.todocomposemvvm.ui.features.home.HomeViewModel
import com.rm.todocomposemvvm.ui.features.home.HomeContract
import com.rm.todocomposemvvm.ui.features.home.composables.HomeScreen
import com.rm.todocomposemvvm.ui.features.task.TaskDetailContract
import com.rm.todocomposemvvm.ui.features.task.TaskViewModel
import com.rm.todocomposemvvm.ui.features.task.composables.TaskScreen
import com.rm.todocomposemvvm.ui.navigation.Route.HOME_ROUTE
import com.rm.todocomposemvvm.ui.navigation.Route.TASK_ROUTE
import com.rm.todocomposemvvm.ui.utils.AppConstants.TASK_ROUTE_ARG_KEY

@Composable
fun AppNavGraph(viewModel: HomeViewModel) {

    val navController: NavHostController = rememberNavController()

    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {

        addHomeComposable(
            viewModel = viewModel,
            onNavigateToTaskScreen = navigationActions.onNavigateToTaskScreen
        )

        addTaskComposable(
            onNavigateToHomeScreen = navigationActions.onNavigateToHomeScreen
        )
    }
}

private fun NavGraphBuilder.addHomeComposable(
    viewModel: HomeViewModel,
    onNavigateToTaskScreen: (taskId: Int) -> Unit
) {
    composable(
        route = HOME_ROUTE
    ) {
        HomeScreen(
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event -> viewModel.setEvent(event) },
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is HomeContract.Effect.Navigation.ToTaskScreen) {
                    onNavigateToTaskScreen(navigationEffect.taskId)
                }
            }
        )
    }
}

private fun NavGraphBuilder.addTaskComposable(
    onNavigateToHomeScreen: () -> Unit
) {
    composable(
        route = TASK_ROUTE,
        arguments = listOf(
            navArgument(TASK_ROUTE_ARG_KEY) { type = NavType.IntType })
    ) {navBackStackEntry ->
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
                    onNavigateToHomeScreen()
                }
            }
        )
    }
}
