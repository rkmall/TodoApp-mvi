package com.rm.todocomposemvvm.ui.navigation

import androidx.navigation.NavController
import com.rm.todocomposemvvm.ui.navigation.ScreenArgument.HOME_ROUTE_ARG_KEY
import com.rm.todocomposemvvm.ui.navigation.ScreenArgument.TASK_ROUTE_ARG_KEY

object ScreenArgument {
    const val HOME_ROUTE_ARG_KEY = "snackBarMessage"
    const val TASK_ROUTE_ARG_KEY = "taskId"
}

sealed class Screen(val route: String) {
    data object Home : Screen(route = "home") {
        fun passMessage(navController: NavController, snackBarMessage: String) {
            navController.previousBackStackEntry?.savedStateHandle?.set(HOME_ROUTE_ARG_KEY, snackBarMessage)
            navController.popBackStack(route, false)
        }
    }

    data object Task : Screen(route = "task/{$TASK_ROUTE_ARG_KEY}") {
        fun passTaskId(taskId: Int): String {
            return "task/$taskId"
        }
    }
}
