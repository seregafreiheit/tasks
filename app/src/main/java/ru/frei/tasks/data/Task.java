package ru.frei.tasks.data;

import androidx.room.ColumnInfo;

public class Task {

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "task")
    private String task;

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }
}
