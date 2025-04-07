package ru.frei.tasks.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TasksEntry.class, ListsEntry.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `lists` (" +
                    " `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `list` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS tasks_new  (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `task` TEXT, `list_id` INTEGER NOT NULL default 0, " +
                    "FOREIGN KEY(`list_id`) REFERENCES `lists`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
            database.execSQL("ALTER TABLE tasks ADD COLUMN list_id INTEGER  DEFAULT 0 NOT NULL");
            database.execSQL("INSERT OR REPLACE INTO tasks_new (id, task, list_id) " +
                    "SELECT id, task, list_id FROM tasks");
            database.execSQL("DROP TABLE tasks");
            database.execSQL("ALTER TABLE tasks_new RENAME TO tasks");
        }
    };
    private static final String DATABASE_NAME = "TasksDatabase";
    private static AppDatabase database;

    public static AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return database;
    }

    public abstract TasksDao tasksDao();

    public abstract ListsDao listsDao();

}
