package com.example.taskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onTaskClicked: (Task) -> Unit,
    private val onTaskDelete: (Task) -> Unit,
    private val onTaskCompletedChanged: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxCompleted: CheckBox = itemView.findViewById(R.id.checkBoxCompleted)
        private val textTitle: TextView = itemView.findViewById(R.id.textViewTaskTitle)
        private val textDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.imageButtonDelete)

        fun bind(task: Task) {
            textTitle.text = task.title
            textDescription.text = task.description ?: ""
            textDescription.visibility = if (task.description.isNullOrBlank()) View.GONE else View.VISIBLE

            checkBoxCompleted.setOnCheckedChangeListener(null)
            checkBoxCompleted.isChecked = task.isCompleted

            checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
                onTaskCompletedChanged(task, isChecked)
            }

            itemView.setOnClickListener {
                onTaskClicked(task)
            }

            buttonDelete.setOnClickListener {
                onTaskDelete(task)
            }
        }
    }
}
