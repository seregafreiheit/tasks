package ru.frei.tasks.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = ListsEntry::class,
        parentColumns = ["id"],
        childColumns = ["list_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TasksEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val task: String,
    @ColumnInfo(name = "list_id")
    var listId: Long = 0
)
