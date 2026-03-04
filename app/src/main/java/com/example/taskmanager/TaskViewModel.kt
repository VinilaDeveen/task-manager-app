package com.example.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val _tasks = MutableLiveData<List<Task>>(emptyList())
    val tasks: LiveData<List<Task>> = _tasks

    init {
        // Load from persistent storage when ViewModel is created.
        _tasks.value = TaskStorage.loadTasks(getApplication())
    }

    fun addTask(title: String, description: String?) {
        if (title.isBlank()) {
            // avoid creating empty tasks.
            return
        }
        val current = _tasks.value.orEmpty()
        val newTask = Task(
            id = System.currentTimeMillis(),
            title = title.trim(),
            description = description?.trim().takeUnless { it.isNullOrBlank() },
            isCompleted = false
        )
        val updated = current + newTask
        updateAndPersist(updated)
    }

    fun updateTask(task: Task) {
        val current = _tasks.value.orEmpty()
        val updated = current.map { if (it.id == task.id) task else it }
        updateAndPersist(updated)
    }

    fun deleteTask(task: Task) {
        val current = _tasks.value.orEmpty()
        val updated = current.filterNot { it.id == task.id }
        updateAndPersist(updated)
    }

    fun toggleCompleted(task: Task, isCompleted: Boolean) {
        updateTask(task.copy(isCompleted = isCompleted))
    }

    private fun updateAndPersist(tasks: List<Task>) {
        _tasks.value = tasks
        TaskStorage.saveTasks(getApplication(), tasks)
    }
}
