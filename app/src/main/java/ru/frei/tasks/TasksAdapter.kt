package ru.frei.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.frei.tasks.TasksAdapter.TasksViewHolder
import ru.frei.tasks.data.TasksEntry
import ru.frei.tasks.databinding.ListItemBinding

class TasksAdapter(val itemClick: (Long) -> Unit) : RecyclerView.Adapter<TasksViewHolder>() {

    private var tasks: List<TasksEntry>? = null
    fun getTasks(): List<TasksEntry>? {
        return tasks
    }

    fun setTasks(list: List<TasksEntry>?) {
        tasks = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val tasksEntry = tasks!![position]
        holder.binding.taskView.apply {
            text = tasksEntry.task
            setOnClickListener {
                itemClick(tasksEntry.id)
            }
        }
    }

    override fun getItemCount(): Int = tasks?.size ?: 0

    inner class TasksViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
