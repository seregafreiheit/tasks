package ru.frei.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import ru.frei.tasks.data.AppDatabase;
import ru.frei.tasks.data.ListsEntry;
import ru.frei.tasks.databinding.FragmentBottomNavigationDrawerBinding;
import ru.frei.tasks.databinding.ListItemBinding;

import static ru.frei.tasks.BottomNavigationFragmentDirections.actionGlobalMainFragment;

public class BottomNavigationFragment extends BottomSheetDialogFragment {

    private FragmentBottomNavigationDrawerBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_navigation_drawer, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppDatabase database = AppDatabase.getInstance(getContext());

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        final ItemAdapter adapter = new ItemAdapter();
        binding.listOfLists.setAdapter(adapter);

        LiveData<List<ListsEntry>> liveData = database.listsDao().loadAllLists();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<ListsEntry>>() {
            @Override
            public void onChanged(List<ListsEntry> listsEntries) {
                adapter.setLists(listsEntries);
            }
        });

        binding.buttonAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("listId", 0);
                navController.navigate(R.id.addListFragment, bundle);
            }
        });
    }


    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private List<ListsEntry> lists;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemBinding listBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.list_item, parent, false);
            return new ViewHolder(listBinding);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ListsEntry listsEntry = lists.get(position);
            String list = listsEntry.getList();
            holder.text.setText(list);
        }

        @Override
        public int getItemCount() {
            if (lists == null) {
                return 0;
            }
            return lists.size();
        }

        private void setLists(List<ListsEntry> lists) {
            this.lists = lists;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            final TextView text;

            ViewHolder(ListItemBinding listItemBinding) {
                super(listItemBinding.getRoot());
                text = listItemBinding.taskView;
                listItemBinding.getRoot().setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int itemId = (int) lists.get(getAdapterPosition()).getId();

                NavigationDirections.ActionGlobalMainFragment action = actionGlobalMainFragment();
                action.setListId(itemId);
                navController.navigate(action);

                dismiss();
            }
        }
    }
}
