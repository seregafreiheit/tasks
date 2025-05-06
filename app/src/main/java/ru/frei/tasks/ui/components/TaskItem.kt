package ru.frei.tasks.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.frei.tasks.data.TasksEntry

@Composable
fun TaskItem(task: TasksEntry, onItemClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(task.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = task.task,
            fontSize = 23.sp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, name = "Task Item Preview")
@Composable
fun PreviewTaskItem() {
    val mockTask = TasksEntry(
        id = 1,
        task = "Проверить превью в Compose",
        listId = 1
    )

    TaskItem(
        task = mockTask,
        onItemClick = { }
    )
}