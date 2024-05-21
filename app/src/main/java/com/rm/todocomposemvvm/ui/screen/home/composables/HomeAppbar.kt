package com.rm.todocomposemvvm.ui.screen.home.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rm.todocomposemvvm.R
import com.rm.todocomposemvvm.data.room.entity.Priority
import com.rm.todocomposemvvm.ui.component.PriorityItem
import com.rm.todocomposemvvm.ui.component.sortPriorityItemList
import com.rm.todocomposemvvm.ui.theme.PaddingMedium
import com.rm.todocomposemvvm.ui.utils.EMPTY_STRING

@Composable
fun HomeAppbar(
    textInput: String,
    searchActive: Boolean,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit,
    onSearchIconClicked: (Boolean) -> Unit,
    onCloseIconClicked: (Boolean) -> Unit,
    onTextInput: (String) -> Unit
) {
    if (searchActive) {
        SearchAppbar(
            textInput = textInput,
            onTextInput = { onTextInput(it) },
            onCloseIconClicked = { onCloseIconClicked(false) }
        )
    } else {
        MainHomeAppbar(
            onSearchClicked = { onSearchIconClicked(true) },
            onSortClicked = { onSortClicked(it) },
            onDeleteClicked = { onDeleteClicked() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeAppbar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.list_screen_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            SearchAction(onSearchClicked = onSearchClicked)
            SortAction(onSortClicked = onSortClicked )
            DeleteAllAction(onDeleteClicked = onDeleteClicked)
        }
    )
}

@Composable
fun SearchAppbar(
    textInput: String,
    onTextInput: (String) -> Unit,
    onCloseIconClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shadowElevation = 0.dp,
        color = Color.Transparent
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars),
            value = textInput,
            onValueChange = { onTextInput(it) },
            placeholder = {
                Text(
                    text = stringResource(R.string.list_screen_searchbar_placeholder),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_tasks),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (textInput.isNotEmpty()) {
                            onTextInput(EMPTY_STRING)
                        } else {
                            onCloseIconClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close_icon)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )
    }
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(
        onClick = { onSearchClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_tasks),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true}) {
        Icon(
            painter = painterResource(R.drawable.ic_filter_list_24),
            contentDescription = stringResource(R.string.sort_tasks),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded ,
            onDismissRequest = { expanded = false }
        ) {
            sortPriorityItemList.forEach { priority ->
                DropdownMenuItem(
                    text = { PriorityItem(priority = priority) },
                    onClick = {
                        expanded = false
                        onSortClicked(priority)
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteAllAction(onDeleteClicked: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded}) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.delete_all),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded ,
            onDismissRequest = { expanded = !expanded }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.padding(start = PaddingMedium),
                        text = stringResource(R.string.delete_all),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                onClick = {
                    expanded = !expanded
                    onDeleteClicked()
                }
            )
        }
    }
}

@Preview
@Composable
private fun AppBarsPreview() {
    MainHomeAppbar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteClicked = {}
    )
}