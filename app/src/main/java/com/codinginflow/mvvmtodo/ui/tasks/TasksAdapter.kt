package com.codinginflow.mvvmtodo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.databinding.ItemTaskBinding

/**
 * Adapter for the TasksList Recycler view
 * */
class TasksAdapter(private val listener: OnItemClickListener): ListAdapter<Task, TasksAdapter.TasksViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    // Object representing each item in the recyclerview list.
    inner class TasksViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                 root.setOnClickListener {
                     val position = adapterPosition
                     if (position != RecyclerView.NO_POSITION) {
                         val task = getItem(position)
                         listener.onItemClick(task)
                     }
                 }
                checkboxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position !=RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkboxCompleted.isChecked)
                    }
                }
            }
        }
        fun bind(task: Task) {
            binding.apply {
                checkboxCompleted.isChecked = task.isCompleted
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.isCompleted
                labelPriority.isVisible = task.isImportant
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    // Handles comparing items on the current list and the newly supplied list.
    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem // both are data classes so the comparison is already implemented
    }
}