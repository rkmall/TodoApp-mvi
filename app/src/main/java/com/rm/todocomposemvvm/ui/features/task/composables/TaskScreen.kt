package com.rm.todocomposemvvm.ui.features.task.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.features.component.Progress
import com.rm.todocomposemvvm.ui.features.task.TaskDetailContract
import com.rm.todocomposemvvm.ui.theme.PaddingExtraSmall
import com.rm.todocomposemvvm.ui.theme.PaddingMedium
import com.rm.todocomposemvvm.ui.theme.PaddingSmall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun TaskScreen(
    state: TaskDetailContract.State,
    effectFlow: Flow<TaskDetailContract.Effect>?,
    onEventSent: (event: TaskDetailContract.Event) -> Unit,
    onNavigationRequested: (TaskDetailContract.Effect.Navigation) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        effectFlow?.collect { effect ->
            when (effect) {
                is TaskDetailContract.Effect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is TaskDetailContract.Effect.Navigation.ToHomeScreen -> onNavigationRequested(effect)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            state.task?.let { task ->
                TaskAppBar(
                    selectedTask = task,
                    onAddClicked = { onEventSent(TaskDetailContract.Event.AddIconClicked(it)) },
                    onUpdateClicked = { onEventSent(TaskDetailContract.Event.UpdateIconClicked(it)) },
                    onDeleteClicked = { onEventSent(TaskDetailContract.Event.DeleteIconClicked(it)) },
                    onBackClicked = { onEventSent(TaskDetailContract.Event.BackIconClicked(it)) },
                )
            }
        },
    ) { paddings ->
        when {
            state.isLoading -> Progress()
            state.isError -> {}
            else -> {
                state.task?.let { task ->
                    TaskContent(
                        modifier = Modifier
                            .padding(paddings),
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
            textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            singleLine = true,
        )
        Spacer(modifier = Modifier.padding(PaddingExtraSmall))
        PriorityDropDownMenu(
            priority = priority,
            onPrioritySelected = { onPrioritySelected(it) }
        )
        Spacer(modifier = Modifier.padding(PaddingExtraSmall))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(10.dp),
            value = description ,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(R.string.description)) },
            textStyle = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
fun TaskScreenPreview() {
    TaskScreen(
        state = TaskDetailContract.State(
            task = TodoTask(1, "My Task", "Task description", Priority.LOW),
            isLoading = false,
            isError = false
        ),
        effectFlow = flowOf(TaskDetailContract.Effect.Navigation.ToHomeScreen("message")),
        onEventSent = {},
        onNavigationRequested = {}
    )
}
