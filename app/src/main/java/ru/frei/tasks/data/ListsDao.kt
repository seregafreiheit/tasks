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
public interface ListsDao {

    @Query("SELECT * FROM lists ORDER BY id")
    LiveData<List<ListsEntry>> loadAllLists();

    @Insert
    long insertList(ListsEntry list);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateList(ListsEntry list);

    @Delete
    void deleteList(ListsEntry list);

    @Query("SELECT * FROM lists WHERE id = :id")
    ListsEntry loadListById(long id);

    @Query("DELETE FROM lists WHERE id >= 2")
    int deleteAllLists();

    @Query("SELECT * FROM lists WHERE id=(SELECT max(id) FROM lists)")
    ListsEntry getMaxId();
}
