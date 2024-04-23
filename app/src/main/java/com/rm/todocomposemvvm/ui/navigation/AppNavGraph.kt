package com.rm.todocomposemvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rm.todocomposemvvm.ui.features.common.TaskOperation
import com.rm.todocomposemvvm.ui.features.home.HomeViewModel
import com.rm.todocomposemvvm.ui.features.home.TodoTaskViewModel
import com.rm.todocomposemvvm.ui.features.home.HomeContract
import com.rm.todocomposemvvm.ui.features.home.composables.HomeScreen
import com.rm.todocomposemvvm.ui.features.task.composables.TaskScreen
import com.rm.todocomposemvvm.ui.utils.AppConstants

@Composable
fun AppNavGraph(
    viewModel: TodoTaskViewModel,
    //viewModel1: TaskListViewModel
) {

    val navController: NavHostController = rememberNavController()

    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Route.LIST_ROUTE,
    ) {

        addListComposable(
            //viewModel = viewModel1,
            navigateToTaskScreen = navigationActions.navigateToTaskScreen
        )

        addTaskComposable(
            viewModel = viewModel,
            navigateToListScreen = navigationActions.navigateToListScreen
        )
    }
}

private fun NavGraphBuilder.addListComposable(
    //viewModel: TaskListViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit = {}
) {
    composable(
        route = Route.LIST_ROUTE,
        arguments = listOf(navArgument(AppConstants.LIST_SCREEN_ARG_KEY) {
            type = NavType.StringType
        })
    ) {

        val viewModel: HomeViewModel = hiltViewModel()

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
    viewModel: TodoTaskViewModel,
    navigateToListScreen: (TaskOperation) -> Unit = {}
) {
    composable(
        route = Route.TASK_ROUTE,
        arguments = listOf(
            navArgument(AppConstants.TASK_SCREEN_ARG_KEY) { type = NavType.IntType }
        )
    ) {navBackStackEntry ->
        val index = navBackStackEntry.arguments!!.getInt(AppConstants.TASK_SCREEN_ARG_KEY)





        val selectedTask by viewModel.clickedTask.collectAsState()
        if (index == -1) {
            LaunchedEffect(key1 = index) {
                viewModel.setClickedTask()
            }
        } else {
            LaunchedEffect(key1 = index) {
                viewModel.getClickedTask(index)
            }
        }

        TaskScreen(
            viewModel = viewModel,
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    }
}