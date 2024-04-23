package com.rm.todocomposemvvm.ui.features.home.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.data.room.entity.TodoTask
import com.rm.todocomposemvvm.ui.base.SIDE_EFFECTS_KEY
import com.rm.todocomposemvvm.ui.features.common.Progress
import com.rm.todocomposemvvm.ui.features.home.HomeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    state: HomeContract.State,
    effectFlow: Flow<HomeContract.Effect>?,
    onEventSent: (event: HomeContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: HomeContract.Effect.Navigation) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val snackBarMessage = stringResource(R.string.todo_tasks_lis_loaded_snackbar_messages)

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is HomeContract.Effect.DataWasLoaded -> {
                    snackbarHostState.showSnackbar(
                        message = snackBarMessage,
                        duration = SnackbarDuration.Short
                    )
                }
                is HomeContract.Effect.Navigation.ToTaskScreen -> onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            HomeAppbar(
                searchTextState = state.homeUiState.appBarUiState.searchText,
                onSortClicked = {},
                onDeleteClicked = {},
                onSearchClicked = {},
                onSearchTextInput = { onEventSent(HomeContract.Event.SearchTextInput(it)) }
            )
        },
        floatingActionButton = {
            HomeFab(navigateToTaskScreen = { taskId -> onEventSent(HomeContract.Event.TaskItemClicked(taskId)) })
        }
    ) { innerPaddings ->
        when {
            state.isLoading -> Progress()
            state.isError -> HomeListEmptyContent()
            else -> {
                HomeListContent(
                    scaffoldPaddingValues = innerPaddings,
                    tasks = state.homeUiState.tasks,
                    navigateToTaskScreen = { taskId -> onEventSent(HomeContract.Event.TaskItemClicked(taskId)) }
                )
            }
        }
    }
}

@Composable
fun HomeListContent(
    scaffoldPaddingValues: PaddingValues,
    tasks: List<TodoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(scaffoldPaddingValues),
        contentPadding = PaddingValues(vertical = 4.dp), // padding before first and after last item
        verticalArrangement = Arrangement.spacedBy(4.dp) // space between each item
    ) {
        items(
            items = tasks,
            key = { task -> task.id }
        ) {task ->
            task.run {
                HomeListItem(
                    id = id,
                    title = title ,
                    description = description ,
                    color = priority.color,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
    }
}

@Composable
fun HomeListEmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier
                .padding(10.dp)
                .size(100.dp),
            onClick = { /*TODO*/ },
        ) {
            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(R.drawable.ic_add_task_24),
                contentDescription = stringResource(R.string.add_task) )
        }
        Text(
            text = "You have no tasks. Add task now!",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview
@Composable
private fun ListScreenPreview() {
    HomeListContent(
        scaffoldPaddingValues = PaddingValues(2.dp),
        tasks = listOf(
            TodoTask(
                1,
                "Cook food",
                "This evening you need to cook yourself! Vegetables are already there, " +
                        "cook meet if you want and get get pickles from the market",
                Priority.LOW
            ),
            TodoTask(
                2,
                "Learn compose",
                "Learn compose side-effects at 5 pm",
                Priority.HIGH
            ),
            TodoTask(
                3,
                "Go running",
                "Go for running in the park for at least 30 mins",
                Priority.MEDIUM
            )
        ),
        navigateToTaskScreen = {}
    )
}

@Preview
@Composable
private fun TodoListEmptyContentPreview() {
    HomeListEmptyContent()
}