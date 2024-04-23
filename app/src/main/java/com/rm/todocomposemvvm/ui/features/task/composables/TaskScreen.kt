package com.rm.todocomposemvvm.ui.features.task.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.ui.base.SIDE_EFFECTS_KEY
import com.rm.todocomposemvvm.ui.features.common.Progress
import com.rm.todocomposemvvm.ui.features.task.TaskDetailContract
import com.rm.todocomposemvvm.ui.theme.PaddingExtraSmall
import com.rm.todocomposemvvm.ui.theme.PaddingMedium
import com.rm.todocomposemvvm.ui.theme.PaddingSmall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun TaskScreen(
    state: TaskDetailContract.State,
    effectFlow: Flow<TaskDetailContract.Effect>?,
    onEventSent: (event: TaskDetailContract.Event) -> Unit,
    onNavigationFrom: () -> Unit,
    onNavigationRequested: (TaskDetailContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is TaskDetailContract.Effect.Navigation.FromListScreen -> onNavigationFrom()
                is TaskDetailContract.Effect.Navigation.ToHomeScreen -> onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        topBar = {
            state.task?.let { task ->
                TaskAppBar(
                    selectedTask = task,
                    onAddClicked = { onEventSent(TaskDetailContract.Event.AddIconClicked(it)) },
                    onUpdateClicked = { onEventSent(TaskDetailContract.Event.UpdateIconClicked(it)) },
                    onDeleteClicked = { onEventSent(TaskDetailContract.Event.DeleteIconClicked(it)) },
                    onBackClicked = { onEventSent(TaskDetailContract.Event.BackButtonClicked) },
                )
            }
        }
    ) { innerPaddings ->
        when {
            state.isLoading -> Progress()
            state.isError -> {}
            else -> {
                state.task?.let { task ->
                    TaskContent(
                        modifier = Modifier.padding(innerPaddings),
                        title = task.title,
                        description = task.description,
                        priority = task.priority,
                        onTitleChange = { onEventSent(TaskDetailContract.Event.TitleTextInput(it)) },
                        onDescriptionChange = { onEventSent(TaskDetailContract.Event.DescriptionTextInput(it)) },
                        onPrioritySelected = { onEventSent(TaskDetailContract.Event.PrioritySelection(it)) },
                    )
                }
            }
        }
    }
}

@Composable
fun TaskContent(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    priority: Priority,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = PaddingMedium,
                top = PaddingSmall,
                end = PaddingMedium,
                bottom = PaddingMedium
            )
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title)) },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true
        )
        Spacer(modifier = Modifier.padding(PaddingExtraSmall))
        PriorityDropDownMenu(
            priority = priority,
            onPrioritySelected = { onPrioritySelected(it) }
        )
        Spacer(modifier = Modifier.padding(PaddingExtraSmall))
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description ,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(R.string.description)) },
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}
