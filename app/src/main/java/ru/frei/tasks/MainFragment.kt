package ru.frei.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.frei.tasks.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = (activity?.application as App).database
                return MainViewModel(database) as T
            }
        }
    }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MainFragmentArgs>()
    private val listId by lazy { args.listId.toLong() }

    private var tasksAdapter = TasksAdapter(::renameTask)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.bottomAppBar)

        setupMenu()
        setupRecyclerView()
        setupViewModel()

        //add new task
        binding.fab.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("listId", listId)
                putBoolean("isUpdate", false)
            }
            findNavController(binding.fab).navigate(R.id.addTaskFragment, bundle)
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.bottomappbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> handleHomeClick()
                    R.id.del_lists_all -> handleDeleteAllLists()
                    R.id.del_list -> handleDeleteList()
                    R.id.del_tasks -> handleDeleteTasks()
                    R.id.rename_list -> handleRenameList()
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun handleHomeClick(): Boolean {
        findNavController(binding.bottomAppBar).navigate(R.id.bottomNavigationFragment)
        return true
    }

    private fun handleDeleteAllLists(): Boolean {
        viewModel.deleteAllLists()
        findNavController(binding.root)
            .navigate(NavigationDirections.actionGlobalMainFragment())
        return true
    }

    private fun handleDeleteList(): Boolean {
        viewModel.deleteList(listId)
        findNavController(binding.root)
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
        findNavController(binding.root).navigate(R.id.addListFragment, bundle)
        return true
    }

    private fun setupRecyclerView() {
        binding.rv.apply {
            adapter = tasksAdapter
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val tasks = tasksAdapter.getTasks()
                    val tasksEntry = tasks?.get(viewHolder.adapterPosition)
                    if (tasksEntry != null) {
                        viewModel.deleteTask(tasksEntry)
                    }
                }
            }).attachToRecyclerView(this)
        }
    }

    private fun setupViewModel() {
        viewModel.getTasksLiveData(listId).observe(viewLifecycleOwner) { tasksEntries ->
            tasksAdapter.setTasks(tasksEntries)
            binding.emptyList.visibility =
                if (tasksEntries.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun renameTask(id: Long) {
        val bundle = Bundle().apply {
            putBoolean("isUpdate", true)
            putLong("listId", listId)
            putLong("taskId", id)
        }
        findNavController(binding.rv).navigate(R.id.addTaskFragment, bundle)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
