package ru.frei.tasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListsEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val list: String
)
