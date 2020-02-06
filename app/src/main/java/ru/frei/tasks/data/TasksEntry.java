package ru.frei.tasks.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TasksEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String task;

    public TasksEntry(int id, String task) {
        this.id = id;
        this.task = task;
    }

    @Ignore
    public TasksEntry(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }


    public int getId() {
        return id;
    }
}
