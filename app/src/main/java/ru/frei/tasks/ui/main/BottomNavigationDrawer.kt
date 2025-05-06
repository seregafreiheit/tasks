package ru.frei.tasks.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import ru.frei.tasks.data.ListsEntry

@Composable
fun BottomNavigationDrawer(
    lists: List<ListsEntry>,
    onNavigateToMain: (Int) -> Unit,
    onNavigateToAddList: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .nestedScroll(rememberNestedScrollInteropConnection()),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(lists) { list ->
                ListItem(
                    headlineContent = { Text(list.list) },
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onNavigateToMain(list.id.toInt())
                            onDismiss()
                        }
                )
            }
        }
        FloatingActionButton(
            onClick = onNavigateToAddList,
            modifier = Modifier
                .align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create list"
            )
        }
    }
}