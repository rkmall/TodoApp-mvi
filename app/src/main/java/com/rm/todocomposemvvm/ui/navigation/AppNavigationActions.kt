package com.rm.todocomposemvvm.ui.navigation

import androidx.navigation.NavController
import com.rm.todocomposemvvm.ui.common.TaskOperation
import com.rm.todocomposemvvm.ui.navigation.Route.LIST_ROUTE

object Route {
    const val LIST_ROUTE = "list/{operation}"
    const val TASK_ROUTE = "task/{taskId}"
}

class AppNavigationActions(navController: NavController) {

    // Nav action from Tasks screen to List screen with argument
    val navigateToListScreen: (TaskOperation) -> Unit = { operation ->
        navController.navigate("list/${operation.name}") {
            popUpTo(LIST_ROUTE) { inclusive = true }
        }
    }

    // Nav action from List screen to Task screen with argument
    val navigateToTaskScreen: (taskId: Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}
