package ru.frei.tasks.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TasksDao {

    @Query("SELECT * FROM tasks ORDER BY id")
    LiveData<List<TasksEntry>> loadAllTasks();

    @Query("SELECT * FROM tasks WHERE list_id = :listId")
    LiveData<List<TasksEntry>> loadAllTasksFrom(long listId);

    @Insert
    void insertTask(TasksEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TasksEntry taskEntry);

    @Delete
    void deleteTask(TasksEntry taskEntry);


    @Query("SELECT * FROM tasks WHERE id = :id")
    TasksEntry loadTaskById(long id);

    @Query("DELETE FROM tasks WHERE list_id = :listId")
    int deleteAllTasksFrom(long listId);
}
