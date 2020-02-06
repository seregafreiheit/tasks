package ru.frei.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.frei.tasks.data.TasksEntry;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<TasksEntry> mTasks;
    private Context context;

    public TasksAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {

        TasksEntry tasksEntry = mTasks.get(position);
        String task = tasksEntry.getTask();

        holder.tw.setText(task);
    }

    @Override
    public int getItemCount() {
        if (mTasks == null) {
            return 0;
        }
        return mTasks.size();
    }

    public List<TasksEntry> getTasks() {
        return mTasks;
    }

    public void setTasks(List<TasksEntry> list) {
        mTasks = list;
        notifyDataSetChanged();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {
        private TextView tw;

        public TasksViewHolder(View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.taskView);
        }
    }
}
