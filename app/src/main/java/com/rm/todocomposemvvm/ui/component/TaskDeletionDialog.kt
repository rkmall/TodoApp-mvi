package com.rm.todocomposemvvm.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TaskDeletionAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    iconDescription: String
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        icon = { Icon(imageVector = icon, contentDescription = iconDescription) },

        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text("Confirm")
            }
        },

        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        },

        onDismissRequest = { onDismissRequest() },
    )
}

object DialogConstants {
    const val DELETE_TASK_TITLE = "Delete task?"
    const val DELETE_TASK_TEXT = "Are you sure you want to delete this task?"
    const val DELETE_ALL_TASKS_TITLE = "Delete all tasks?"
    const val DELETE_ALL_TASKS_TEXT = "Are you sure you want to delete all tasks?"
    const val DELETE_ICON_DESCRIPTION = "Delete"
}
