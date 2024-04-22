package com.rm.todocomposemvvm.ui.navigation

import androidx.navigation.NavController
import com.rm.todocomposemvvm.ui.common.TaskOperation
import com.rm.todocomposemvvm.ui.navigation.Route.LIST_ROUTE

object Route {
    const val LIST_ROUTE = "list/{operation}"
    const val TASK_ROUTE = "task/{taskId}"
}

class AppNavigationActions(navController: NavController) {

    val navigateToListScreen: (TaskOperation) -> Unit = { operation ->
        navController.navigate("list/${operation.name}") {
            popUpTo(LIST_ROUTE) { inclusive = true }
        }
    }

    val navigateToTaskScreen: (taskId: Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}
