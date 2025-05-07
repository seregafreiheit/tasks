package ru.frei.tasks.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.frei.tasks.data.TasksEntry
import ru.frei.tasks.ui.components.TaskItem

@Composable
fun MainScreen(
    tasks: List<TasksEntry>,
    onAddTask: () -> Unit,
    onAllListsClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onDeleteTask: (TasksEntry) -> Unit,
    onDeleteAllLists: () -> Unit,
    onDeleteList: () -> Unit,
    onDeleteTasks: () -> Unit,
    onRenameList: () -> Unit
) {
    var showActionMenu by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { showActionMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Show actions"
                        )
                    }

                    DropdownMenu(
                        expanded = showActionMenu,
                        onDismissRequest = { showActionMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete all lists") },
                            onClick = {
                                onDeleteAllLists()
                                showActionMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete this list") },
                            onClick = {
                                onDeleteList()
                                showActionMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete tasks") },
                            onClick = {
                                onDeleteTasks()
                                showActionMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Rename list") },
                            onClick = {
                                onRenameList()
                                showActionMenu = false
                            }
                        )
                    }

                    IconButton(onClick = onAllListsClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open navigation"
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onAddTask,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (tasks.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Click + to add a task",
                    fontSize = 30.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(tasks) { task ->
                    SwipeToDeleteItem(
                        onDelete = { onDeleteTask(task) }
                    ) {
                        TaskItem(task = task, onItemClick = onTaskClick)
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        positionalThreshold = { distance -> distance * 0.5f }
    )

    SwipeToDismissBox(
        state = state,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = true,
        backgroundContent = {
            val direction = state.dismissDirection

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(horizontal = 16.dp),
                contentAlignment = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Icon(
                        Icons.Default.Check, "Complete",
                        tint = Color.Green
                    )

                    SwipeToDismissBoxValue.EndToStart -> Icon(
                        Icons.Default.Delete, "Delete",
                        tint = Color.Red
                    )

                    else -> Unit
                }
            }
        }
    ) {
        content()
    }

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart ||
            state.currentValue == SwipeToDismissBoxValue.StartToEnd
        ) {
            onDelete()
            state.reset()
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5", showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen(
            tasks = listOf(
                TasksEntry(id = 1, task = "Первая задача", listId = 1),
                TasksEntry(id = 2, task = "Вторая задача", listId = 1),
                TasksEntry(
                    id = 3,
                    task = "Длинное название задачи для проверки переноса текста",
                    listId = 1
                )
            ),
            onAddTask = {},
            onAllListsClick = {},
            onTaskClick = {},
            onDeleteTask = {},
            onDeleteList = {},
            onRenameList = {},
            onDeleteTasks = {},
            onDeleteAllLists = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
fun MainScreenEmptyPreview() {
    MaterialTheme {
        MainScreen(
            tasks = emptyList(),
            onAddTask = {},
            onAllListsClick = {},
            onTaskClick = {},
            onDeleteTask = {},
            onDeleteList = {},
            onRenameList = {},
            onDeleteTasks = {},
            onDeleteAllLists = {}
        )
    }
}