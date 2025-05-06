package ru.frei.tasks.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.frei.tasks.data.TasksEntry
import ru.frei.tasks.ui.main.InputBottomSheetContent

@AndroidEntryPoint
class AddTaskFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listId = requireArguments().getLong("listId")
        val isUpdate = requireArguments().getBoolean("isUpdate")
        val taskId = requireArguments().getLong("taskId", 0)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    var loadedText by remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(Unit) {
                        loadedText = if (isUpdate) {
                            viewModel.loadTaskById(taskId).task
                        } else {
                            ""
                        }
                    }

                    loadedText?.let { text ->
                        InputBottomSheetContent(
                            initialText = text,
                            hint = "New task",
                            onActionDone = { text ->
                                if (isUpdate) {
                                    viewModel.updateTask(TasksEntry(taskId, text, listId))
                                } else {
                                    viewModel.insertTask(TasksEntry(task = text, listId = listId))
                                }
                                dismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}