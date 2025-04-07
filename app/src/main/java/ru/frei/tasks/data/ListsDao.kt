package ru.frei.tasks.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListsDao {

    @Query("SELECT * FROM lists ORDER BY id")
    fun loadAllLists(): LiveData<List<ListsEntry>>

    @Insert
    suspend fun insertList(list: ListsEntry): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateList(list: ListsEntry)

    @Delete
    suspend fun deleteList(list: ListsEntry)

    @Query("SELECT * FROM lists WHERE id = :id")
    suspend fun loadListById(id: Long): ListsEntry

    @Query("DELETE FROM lists WHERE id >= 2")
    suspend fun deleteAllLists(): Int

    @Query("SELECT COUNT(*) FROM lists")
    suspend fun getCount(): Int
}
