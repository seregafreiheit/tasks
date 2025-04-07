package ru.frei.tasks

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `lists` (" +
                    " `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `list` TEXT)"
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS tasks_new  (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `task` TEXT, `list_id` INTEGER NOT NULL default 0, " +
                    "FOREIGN KEY(`list_id`) REFERENCES `lists`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )"
        )
        db.execSQL("ALTER TABLE tasks ADD COLUMN list_id INTEGER  DEFAULT 0 NOT NULL")
        db.execSQL(
            "INSERT OR REPLACE INTO tasks_new (id, task, list_id) " +
                    "SELECT id, task, list_id FROM tasks"
        )
        db.execSQL("DROP TABLE tasks")
        db.execSQL("ALTER TABLE tasks_new RENAME TO tasks")
    }
}
