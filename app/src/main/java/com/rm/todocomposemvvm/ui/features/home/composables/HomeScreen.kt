package com.rm.todocomposemvvm.ui.features.home.composables

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.rm.todocomposemvvm.ui.features.component.Progress
import com.rm.todocomposemvvm.ui.features.home.AppBarUiState
import com.rm.todocomposemvvm.ui.features.home.HomeContract
import com.rm.todocomposemvvm.ui.features.home.HomeUiState
import com.rm.todocomposemvvm.ui.theme.PaddingExtraSmall
import com.rm.todocomposemvvm.ui.utils.AppConstants.DEFAULT_TASK_ID
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    state: HomeContract.State,
    effectFlow: Flow<HomeContract.Effect>?,
    onEventSent: (event: HomeContract.Event) -> Unit,
    snackBarMessage: String = EMPTY_STRING,
    onNavigationRequested: (navigationEffect: HomeContract.Effect.Navigation) -> Unit
) {
    Log.d("screen", "Home Screen Called")
    val snackBarHostState = remember { SnackbarHostState() }

    val message by remember { mutableStateOf(snackBarMessage) }

    LaunchedEffect(key1 = message) {
        effectFlow?.collect { effect ->
            when (effect) {
                is HomeContract.Effect.DataWasLoaded -> {
                    if (message.isNotEmpty()) {
                        snackBarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                is HomeContract.Effect.Navigation.ToTaskScreen -> onNavigationRequested(effect)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            HomeAppbar(
                textInput = state.homeUiState.appBarUiState.searchText,
                onSortClicked = {},
                onDeleteClicked = {},
                onSearchClicked = {},
                onTextInput = { onEventSent(HomeContract.Event.SearchTextInput(it)) }
            )
        },
        floatingActionButton = {
            HomeFab(onNavigateToTaskScreen = { onEventSent(HomeContract.Event.TaskItemClicked(it)) })
        }
    ) { innerPaddings ->
        when {
            state.isLoading -> Progress()
            state.isError -> HomeEmptyContent(
                onNavigateToTaskScreen = {
                    onEventSent(HomeContract.Event.TaskItemClicked(it))
                }
            )
            else -> {
                HomeContent(
                    modifier = Modifier.padding(innerPaddings),
                    tasks = state.homeUiState.tasks,
                    onNavigateToTaskScreen = { onEventSent(HomeContract.Event.TaskItemClicked(it)) }
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    tasks: List<TodoTask>,
    onNavigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = PaddingExtraSmall), // padding before first and after last item
        verticalArrangement = Arrangement.spacedBy(PaddingExtraSmall) // space between each item
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
                    onNavigateToTaskScreen = onNavigateToTaskScreen
                )
            }
        }
    }
}

@Composable
fun HomeEmptyContent(onNavigateToTaskScreen: (taskId: Int) -> Unit) {
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
            onClick = { onNavigateToTaskScreen(DEFAULT_TASK_ID) },
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
private fun HomeContentPreview() {
    HomeContent(
        modifier = Modifier.padding(2.dp),
        tasks = listOf(
            TodoTask(
                1,
                "Cook food",
                "This evening you need to cook yourself! Vegetables are already there.",
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
        onNavigateToTaskScreen = {}
    )
}

@Preview
@Composable
private fun HomeEmptyContentPreview() {
    HomeEmptyContent(
        onNavigateToTaskScreen = {}
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val homeUiState = HomeUiState(
        tasks = listOf(
            TodoTask(
                1,
                "Cook food",
                "This evening you need to cook yourself! Vegetables are already there.",
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
        appBarUiState = AppBarUiState()
    )
    HomeScreen(
        state = HomeContract.State(homeUiState, isLoading = false, isError = false),
        effectFlow = flowOf(HomeContract.Effect.DataWasLoaded),
        onEventSent = {},
        snackBarMessage = "",
        onNavigationRequested = {}
    )
}
