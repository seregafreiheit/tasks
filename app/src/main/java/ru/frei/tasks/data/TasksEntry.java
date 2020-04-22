package ru.frei.tasks.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "tasks", foreignKeys = @ForeignKey(entity = ListsEntry.class,
        parentColumns = "id", childColumns = "list_id", onDelete = CASCADE))
public class TasksEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String task;
    private long list_id;

    public TasksEntry(long id, String task) {
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

    public long getId() {
        return id;
    }

    public long getList_id() {
        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }
}
