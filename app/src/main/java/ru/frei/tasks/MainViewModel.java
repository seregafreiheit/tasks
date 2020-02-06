package ru.frei.tasks;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ru.frei.tasks.data.AppDatabase;
import ru.frei.tasks.data.TasksEntry;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<TasksEntry>> tasks;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        tasks = database.tasksDao().loadAllTasks();
    }

    public LiveData<List<TasksEntry>> getTasks() {
        return tasks;
    }
}
