package ru.frei.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.frei.tasks.data.TasksEntry

class AddTaskFragment : InputBottomSheet() {

    private val viewModel by activityViewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = (activity?.application as App).database
                return MainViewModel(database) as T
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listID = requireArguments().getLong("listId")
        val isUpdate = requireArguments().getBoolean("isUpdate")
        val taskID = requireArguments().getLong("taskId", 0)

        lifecycleScope.launch {
            val initialText = if (isUpdate) {
                viewModel.loadTaskById(taskID).task
            } else {
                null
            }

            setupInput(initialText) { text ->
                if (isUpdate) {
                    viewModel.updateTask(TasksEntry(taskID, text, listID))
                } else {
                    viewModel.insertTask(TasksEntry(task = text, listId = listID))
                }
            }
        }
    }
}
