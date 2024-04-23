package com.rm.todocomposemvvm.ui.features.home.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.ui.theme.Purple80

@Composable
fun HomeFab(navigateToTaskScreen: (taskId: Int) -> Unit) {
    FloatingActionButton(
        onClick = { navigateToTaskScreen(-1) },
        containerColor = Purple80
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = Color.DarkGray
        )
    }
}
