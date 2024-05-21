package com.rm.todocomposemvvm.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.rm.todocomposemvvm.ui.navigation.RouteArgument.HOME_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.navigation.RouteArgument.TASK_ROUTE_ARG_KEY

object RouteArgument {
    const val HOME_ROUTE_ARG_KEY = "snackBarMessage"
    const val TASK_ROUTE_ARG_KEY = "taskId"
}

sealed class Route(val route: String) {
    data object Home : Route(route = "home") {
        fun passSnackBarMessage(navController: NavController, snackBarMessage: String) {
            navController.previousBackStackEntry?.savedStateHandle?.set(HOME_ROUTE_ARG_KEY, snackBarMessage)
            navController.popBackStack(route, false)
        }
    }

    data object Task : Route(route = "task/{$TASK_ROUTE_ARG_KEY}") {
        fun passTaskId(navController: NavHostController, taskId: Int) {
            navController.navigate("task/$taskId")
        }
    }
}
