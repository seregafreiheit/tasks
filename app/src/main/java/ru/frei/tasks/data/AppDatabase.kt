package ru.frei.tasks.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TasksEntry::class, ListsEntry::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun listsDao(): ListsDao
}