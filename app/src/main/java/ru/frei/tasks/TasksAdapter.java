package ru.frei.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.frei.tasks.data.TasksEntry;
import ru.frei.tasks.databinding.ListItemBinding;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private ItemClickListener itemClickListener;
    private List<TasksEntry> tasks;
    private Context context;

    public TasksAdapter(Context context, ItemClickListener clickListener) {
        this.context = context;
        itemClickListener = clickListener;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false);
        return new TasksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        TasksEntry tasksEntry = tasks.get(position);
        String task = tasksEntry.getTask();
        holder.tw.setText(task);
    }

    @Override
    public int getItemCount() {
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }

    public List<TasksEntry> getTasks() {
        return tasks;
    }

    public void setTasks(List<TasksEntry> list) {
        tasks = list;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(long itemID);
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tw;

        public TasksViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            tw = binding.taskView;
            tw.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long itemId = tasks.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(itemId);

        }
    }
}
