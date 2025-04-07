package ru.frei.tasks.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lists")
public class ListsEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String list;

    public ListsEntry(long id, String list) {
        this.id = id;
        this.list = list;
    }

    @Ignore
    public ListsEntry(String list) {
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public String getList() {
        return list;
    }
}
