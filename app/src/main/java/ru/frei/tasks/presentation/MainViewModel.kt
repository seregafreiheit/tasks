package ru.frei.tasks.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.frei.tasks.data.AppDatabase
import ru.frei.tasks.data.ListsEntry
import ru.frei.tasks.data.TasksEntry
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {

    fun getListsLiveData(): LiveData<List<ListsEntry>> {
        return database.listsDao().loadAllLists()
    }

    fun getTasksLiveData(listId: Long): LiveData<List<TasksEntry>> {
        return database.tasksDao().loadAllTasksFrom(listId)
    }

    fun deleteAllLists() {
        viewModelScope.launch {
            database.listsDao().deleteAllLists()
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch {
            val listsEntry = database.listsDao().loadListById(listId)
            if (listId == 1L) {
                return@launch
            }
            database.listsDao().deleteList(listsEntry)
        }
    }

    fun insertList(listsEntry: ListsEntry) {
        viewModelScope.launch {
            database.listsDao().insertList(listsEntry)
        }
    }

    fun deleteTasks(listId: Long) {
        viewModelScope.launch {
            database.tasksDao().deleteAllTasksFrom(listId)
        }
    }

    fun renameList(listsEntry: ListsEntry) {
        viewModelScope.launch {
            database.listsDao().updateList(listsEntry)
        }
    }

    fun deleteTask(tasksEntry: TasksEntry) {
        viewModelScope.launch {
            database.tasksDao().deleteTask(tasksEntry)
        }
    }

    fun insertTask(tasksEntry: TasksEntry) {
        viewModelScope.launch {
            database.tasksDao().insertTask(tasksEntry)
        }
    }

    fun updateTask(tasksEntry: TasksEntry) {
        viewModelScope.launch {
            database.tasksDao().updateTask(tasksEntry)
        }
    }

    suspend fun loadTaskById(id: Long): TasksEntry {
        return viewModelScope.async {
            database.tasksDao().loadTaskById(id)
        }.await()
    }

    suspend fun getListById(id: Long): ListsEntry {
        return viewModelScope.async {
            database.listsDao().loadListById(id)
        }.await()
    }
}