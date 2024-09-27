package ru.frei.tasks.data;

import ru.frei.tasks.AppExecutors;
import ru.frei.tasks.MainActivity;

public class DatabaseHelper {
    private static AppDatabase database = MainActivity.database;
    private static long newID;

    public static void initMainList() {
        final ListsEntry listsEntry = new ListsEntry(1, "Main list");
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.listsDao().insertList(listsEntry);
            }
        });
    }

    public static void deleteAllLists() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.listsDao().deleteAllLists();
            }
        });
    }

    public static void deleteList(final long listId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                ListsEntry listsEntry = database.listsDao().loadListById(listId);
                if (listId == 1) {
                    return;
                }
                database.listsDao().deleteList(listsEntry);
            }
        });
    }

    public static long insertList(final ListsEntry listsEntry) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                newID = database.listsDao().insertList(listsEntry);
            }
        });
        return newID;
    }

    public static void deleteTasks(final long listId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.tasksDao().deleteAllTasksFrom(listId);
            }
        });
    }

    public static void renameList(final ListsEntry listsEntry) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.listsDao().updateList(listsEntry);
            }
        });
    }

    public static void deleteTask(final TasksEntry tasksEntry) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.tasksDao().deleteTask(tasksEntry);
            }
        });
    }
}
