package ru.frei.tasks.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.frei.tasks.NavigationDirections
import ru.frei.tasks.R
import ru.frei.tasks.data.TasksEntry
import ru.frei.tasks.ui.main.MainScreen
import ru.frei.tasks.ui.theme.AppTheme

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    private val args by navArgs<MainFragmentArgs>()
    private val listId by lazy { args.listId.toLong() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val tasks by viewModel.getTasksLiveData(listId).observeAsState(emptyList())

                    MainScreen(
                        tasks = tasks,
                        onAddTask = ::addTask,
                        onAllListsClick = ::handleHomeClick,
                        onTaskClick = ::renameTask,
                        onDeleteTask = ::deleteTask,
                        onDeleteAllLists = ::handleDeleteAllLists,
                        onDeleteList = ::handleDeleteList,
                        onDeleteTasks = ::handleDeleteTasks,
                        onRenameList = ::handleRenameList
                    )
                }
            }
        }
    }

    private fun handleHomeClick(): Boolean {
        findNavController().navigate(R.id.bottomNavigationFragment)
        return true
    }

    private fun handleDeleteAllLists(): Boolean {
        viewModel.deleteAllLists()
        findNavController()
            .navigate(NavigationDirections.actionGlobalMainFragment())
        return true
    }

    private fun handleDeleteList(): Boolean {
        viewModel.deleteList(listId)
        findNavController()
            .navigate(NavigationDirections.actionGlobalMainFragment())
        return true
    }

    private fun handleDeleteTasks(): Boolean {
        viewModel.deleteTasks(listId)
        return true
    }

    private fun handleRenameList(): Boolean {
        val bundle = Bundle().apply {
            putLong("listId", listId)
        }
        findNavController().navigate(R.id.addListFragment, bundle)
        return true
    }

    private fun addTask() {
        findNavController().navigate(
            R.id.addTaskFragment,
            bundleOf("listId" to listId, "isUpdate" to false)
        )
    }

    private fun renameTask(id: Long) {
        val bundle = Bundle().apply {
            putBoolean("isUpdate", true)
            putLong("listId", listId)
            putLong("taskId", id)
        }
        findNavController().navigate(R.id.addTaskFragment, bundle)
    }

    private fun deleteTask(task: TasksEntry) = viewModel.deleteTask(task)
}