package com.example.studyplanner

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.taskDataStore by preferencesDataStore(name = "study_tasks")
private val TASKS_KEY = stringPreferencesKey("tasks")

data class Task(val title: String, val isCompleted: Boolean = false)

class TaskStorage(context: Context) {
    private val dataStore = context.taskDataStore

    val tasks = dataStore.data.map { preferences ->
        readTasks(preferences[TASKS_KEY] ?: "[]")
    }

    suspend fun addTask(title: String) {
        dataStore.edit { preferences ->
            val tasks = readTasks(preferences[TASKS_KEY] ?: "[]")
            tasks.add(Task(title))
            preferences[TASKS_KEY] = writeTasks(tasks)
        }
    }

    suspend fun setCompleted(index: Int, completed: Boolean) {
        dataStore.edit { preferences ->
            val tasks = readTasks(preferences[TASKS_KEY] ?: "[]")
            tasks[index] = tasks[index].copy(isCompleted = completed)
            preferences[TASKS_KEY] = writeTasks(tasks)
        }
    }

    private fun readTasks(json: String): MutableList<Task> {
        val array = JSONArray(json)
        val tasks = mutableListOf<Task>()
        for (index in 0 until array.length()) {
            val item = array.getJSONObject(index)
            tasks.add(Task(item.getString("title"), item.getBoolean("isCompleted")))
        }
        return tasks
    }

    private fun writeTasks(tasks: List<Task>): String {
        val array = JSONArray()
        for (task in tasks) {
            val item = JSONObject()
            item.put("title", task.title)
            item.put("isCompleted", task.isCompleted)
            array.put(item)
        }
        return array.toString()
    }
}
