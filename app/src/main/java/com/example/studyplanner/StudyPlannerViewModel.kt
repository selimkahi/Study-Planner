package com.example.studyplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Handles task actions and gives the screen its saved task list.
class StudyPlannerViewModel(private val storage: TaskStorage) : ViewModel() {

    // Shares the saved list while the screen observes it.
    // The short delay keeps it active during a quick screen recreation.
    val tasks = storage.tasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // Removes extra spaces and saves a non-empty task.
    fun addTask(title: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return

        // This saving work belongs to the ViewModel.
        viewModelScope.launch {
            storage.addTask(cleanTitle)
        }
    }

    // Saves the new checkbox value for the selected task.
    fun setCompleted(index: Int, checked: Boolean) {
        viewModelScope.launch {
            storage.setCompleted(index, checked)
        }
    }
}
