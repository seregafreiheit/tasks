package ru.frei.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.frei.tasks.data.AppDatabase;
import ru.frei.tasks.data.DatabaseHelper;
import ru.frei.tasks.data.TasksEntry;
import ru.frei.tasks.databinding.FragmentMainBinding;

import static ru.frei.tasks.BottomNavigationFragmentDirections.actionGlobalMainFragment;

public class MainFragment extends Fragment implements TasksAdapter.ItemClickListener {

    private long listId;
    private FragmentMainBinding binding;
    private TasksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //get listId from navigation action
        MainFragmentArgs args = MainFragmentArgs.fromBundle(getArguments());
        listId = args.getListId();

        //set binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main,
                container, false);

        // set BottomAppBar with options menu
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.bottomAppBar);
        setHasOptionsMenu(true);

        // recycler view and adapter
        adapter = new TasksAdapter(getContext(), this);
        binding.rv.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                List<TasksEntry> tasks = adapter.getTasks();
                TasksEntry tasksEntry = tasks.get(viewHolder.getAdapterPosition());
                DatabaseHelper.deleteTask(tasksEntry);
            }
        }).attachToRecyclerView(binding.rv);

        setupViewModel();

        //add new task
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("listId", listId);
                bundle.putBoolean("isUpdate", false);
                Navigation.findNavController(binding.fab).navigate(R.id.addTaskFragment, bundle);
            }
        });

        return binding.getRoot();
    }

    private void setupViewModel() {
        AppDatabase database = AppDatabase.getInstance(getContext());
        MainViewModelFactory factory = new MainViewModelFactory(database, listId);
        final MainViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(MainViewModel.class);

        //get list of tasks for adapter
        viewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<TasksEntry>>() {
            @Override
            public void onChanged(@Nullable List<TasksEntry> tasksEntries) {
                adapter.setTasks(tasksEntries);
                if (tasksEntries.size() == 0) {
                    binding.emptyList.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyList.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override // rename task
    public void onItemClickListener(long taskId) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isUpdate", true);
        bundle.putLong("listId", listId);
        bundle.putLong("taskId", taskId);
        Navigation.findNavController(binding.rv).navigate(R.id.addTaskFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.bottomappbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (android.R.id.home):
                Navigation.findNavController(binding.bottomAppBar).navigate(R.id.bottomNavigationFragment);
                break;

            case (R.id.del_lists_all):
                DatabaseHelper.deleteAllLists();
                Navigation.findNavController(binding.getRoot()).navigate(actionGlobalMainFragment());
                break;

            case (R.id.del_list):
                DatabaseHelper.deleteList(listId);
                Navigation.findNavController(binding.getRoot()).navigate(actionGlobalMainFragment());
                break;

            case (R.id.del_tasks):
                DatabaseHelper.deleteTasks(listId);
                break;

            case (R.id.rename_list):
                Bundle bundle = new Bundle();
                bundle.putLong("listId", listId);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.addListFragment, bundle);
                break;
        }
        return true;
    }
}
