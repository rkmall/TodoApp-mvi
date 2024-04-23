package com.rm.todocomposemvvm.ui.navigation

import androidx.navigation.NavController
import com.rm.todocomposemvvm.ui.navigation.Route.HOME_ROUTE

object Route {
    const val HOME_ROUTE = "home"
    const val TASK_ROUTE = "task/{taskId}"
}

class AppNavigationActions(navController: NavController) {

    val navigateToListScreen: () -> Unit = {
        navController.navigate(HOME_ROUTE) {
            popUpTo(HOME_ROUTE)
        }
    }

    val navigateToTaskScreen: (taskId: Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}
