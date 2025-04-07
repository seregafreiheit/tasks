package ru.frei.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.frei.tasks.data.AppDatabase;
import ru.frei.tasks.data.TasksEntry;

public class MainViewModel extends ViewModel {

    private LiveData<List<TasksEntry>> tasks;

    public MainViewModel(AppDatabase database, long listId) {
        tasks = database.tasksDao().loadAllTasksFrom(listId);
    }

    public LiveData<List<TasksEntry>> getTasks() {
        return tasks;
    }
}
