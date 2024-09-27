package ru.frei.tasks;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.frei.tasks.data.AppDatabase;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase database;
    private final long listId;

    public MainViewModelFactory(AppDatabase database, long listId) {
        this.database = database;
        this.listId = listId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(database, listId);
    }
}
