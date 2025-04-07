package ru.frei.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.frei.tasks.data.ListsEntry

class AddListFragment : InputBottomSheet() {

    private val viewModel by activityViewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = (activity?.application as App).database
                return MainViewModel(database) as T
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listId = requireArguments().getLong("listId", 0)

        lifecycleScope.launch {
            val initialText = if (listId != 0L) {
                viewModel.getListById(listId).list
            } else {
                null
            }

            setupInput(initialText) { text ->
                if (listId == 0L) {
                    viewModel.insertList(ListsEntry(list = text))
                } else {
                    viewModel.renameList(ListsEntry(listId, text))
                }
            }
        }
    }
}