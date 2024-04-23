package com.rm.todocomposemvvm.ui.navigation

import android.util.Log
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
import com.rm.todocomposemvvm.ui.utils.AppConstants

@Composable
fun AppNavGraph(viewModel: HomeViewModel) {

    val navController: NavHostController = rememberNavController()

    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Route.HOME_ROUTE,
    ) {

        addHomeComposable(
            viewModel = viewModel,
            navigateToTaskScreen = navigationActions.navigateToTaskScreen
        )

        addTaskComposable(
            navigateToListScreen = navigationActions.navigateToListScreen
        )
    }
}

private fun NavGraphBuilder.addHomeComposable(
    viewModel: HomeViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    composable(
        route = Route.HOME_ROUTE
    ) {
        HomeScreen(
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event -> viewModel.setEvent(event)  },
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is HomeContract.Effect.Navigation.ToTaskScreen) {
                    navigateToTaskScreen(navigationEffect.taskId)
                }
            }
        )
    }
}

private fun NavGraphBuilder.addTaskComposable(
    navigateToListScreen: () -> Unit
) {
    composable(
        route = Route.TASK_ROUTE,
        arguments = listOf(
            navArgument(AppConstants.TASK_SCREEN_ARG_KEY) { type = NavType.IntType }
        )
    ) {navBackStackEntry ->
        val argument = requireNotNull( navBackStackEntry.arguments)
        val taskId = argument.getInt(AppConstants.TASK_SCREEN_ARG_KEY)

        val viewModel = hiltViewModel<TaskViewModel>()

        TaskScreen(
            state = viewModel.viewState.value,
            effectFlow = viewModel.effect,
            onEventSent = { event -> viewModel.setEvent(event) },
            onNavigationFrom = {
                Log.d("todo:", "viewModel called")
                viewModel.getSelectedTask(taskId)
            },
            onNavigationRequested = { navigationEffect ->
                if (navigationEffect is TaskDetailContract.Effect.Navigation.ToHomeScreen) {
                    navigateToListScreen()
                }
            }
        )
    }
}