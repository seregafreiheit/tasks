package ru.frei.tasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.frei.tasks.MIGRATION_1_2

@Database(
    entities = [TasksEntry::class, ListsEntry::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao
    abstract fun listsDao(): ListsDao

    companion object {
        private const val DATABASE_NAME = "TasksDatabase"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            )
                .addCallback(DatabaseCallback(context))
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val dao = buildDatabase(context).listsDao()
                if (dao.getCount() == 0) {
                    val defaultList = ListsEntry(list = "Main list")
                    dao.insertList(defaultList)
                }
            }
        }
    }
}
