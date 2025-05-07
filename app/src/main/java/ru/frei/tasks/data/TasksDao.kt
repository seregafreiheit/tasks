package ru.frei.tasks.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks WHERE list_id = :listId")
    fun loadAllTasksFrom(listId: Long): LiveData<List<TasksEntry>>

    @Insert
    suspend fun insertTask(taskEntry: TasksEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(taskEntry: TasksEntry)

    @Delete
    suspend fun deleteTask(taskEntry: TasksEntry)


    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun loadTaskById(id: Long): TasksEntry

    @Query("DELETE FROM tasks WHERE list_id = :listId")
    suspend fun deleteAllTasksFrom(listId: Long): Int
}
