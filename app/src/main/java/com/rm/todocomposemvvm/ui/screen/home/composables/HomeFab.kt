package com.rm.todocomposemvvm.ui.screen.home.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.ui.theme.Purple80
import com.rm.todocomposemvvm.ui.utils.AppConstants.DEFAULT_TASK_ID

@Composable
fun HomeFab(onNavigateToTaskScreen: (taskId: Int) -> Unit) {
    FloatingActionButton(
        onClick = { onNavigateToTaskScreen(DEFAULT_TASK_ID) },
        containerColor = Purple80
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = Color.DarkGray
        )
    }
}
