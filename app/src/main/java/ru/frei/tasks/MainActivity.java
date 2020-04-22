package ru.frei.tasks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import ru.frei.tasks.data.AppDatabase;
import ru.frei.tasks.data.DatabaseHelper;
import ru.frei.tasks.data.ListsEntry;

// This main activity is just a container for fragments
public class MainActivity extends AppCompatActivity {

    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // layout with navigation graph
        setContentView(R.layout.activity_main);
        initMainList();
    }


    public void initMainList() {
        database = AppDatabase.getInstance(this);
        final LiveData<List<ListsEntry>> liveData = database.listsDao().loadAllLists();
        liveData.observe(this, new Observer<List<ListsEntry>>() {
            @Override
            public void onChanged(List<ListsEntry> listsEntries) {
                if (listsEntries.size() == 0) {
                    DatabaseHelper.initMainList();
                }
                liveData.removeObserver(this);
            }
        });
    }
}
