package com.example.taskmanager

import android.content.Context
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

object TaskStorage {

    private const val PREF_NAME = "tasks_prefs"
    private const val KEY_TASKS = "tasks_json"

    fun loadTasks(context: Context): List<Task> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TASKS, null) ?: return emptyList()
        val array = JSONArray(json)
        val tasks = mutableListOf<Task>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            tasks.add(
                Task(
                    id = obj.getLong("id"),
                    title = obj.getString("title"),
                    description = if (obj.has("description") && !obj.isNull("description")) obj.getString("description") else null,
                    isCompleted = obj.getBoolean("isCompleted")
                )
            )
        }
        return tasks
    }

    fun saveTasks(context: Context, tasks: List<Task>) {
        val array = JSONArray()
        tasks.forEach { task ->
            val obj = JSONObject().apply {
                put("id", task.id)
                put("title", task.title)
                put("description", task.description)
                put("isCompleted", task.isCompleted)
            }
            array.put(obj)
        }
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_TASKS, array.toString())
        }
    }
}
