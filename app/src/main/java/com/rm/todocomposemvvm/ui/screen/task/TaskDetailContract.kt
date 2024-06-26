package com.rm.todocomposemvvm.ui.screen.task

import android.view.View
import androidx.compose.ui.graphics.vector.ImageVector
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.base.ViewEvent
import com.rm.todocomposemvvm.ui.base.ViewSideEffect
import com.rm.todocomposemvvm.ui.base.ViewState

class TaskDetailContract {

    sealed class Event : ViewEvent {
        data class AddIconClicked(val task: TodoTask): Event()
        data class UpdateIconClicked(val task: TodoTask): Event()
        data class DeleteIconClicked(val task: TodoTask): Event()
        data class ConfirmDeletion(val task: TodoTask) : Event()
        data class BackIconClicked(val message: String) : Event()
        data class TitleTextInput(val title: String) : Event()
        data class DescriptionTextInput(val description: String) : Event()
        data class PrioritySelection(val priority: Priority) : Event()
    }

    data class State(
        val task: TodoTask?,
        val isLoading: Boolean,
        val isError: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {

        data class ShowSnackBar(val message: String) : Effect()

        data class ShowAlertDialog(val task: TodoTask) : Effect()

        sealed class Navigation : Effect() {
            data class ToHomeScreen(val snackBarMessage: String) : Navigation()
        }
    }
}
