package com.delly.journeyjournal.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.delly.journeyjournal.db.JournalRepository

class CreateJourneyViewModel(
    private val repository: JournalRepository
) : ViewModel() {

    // Load in the data from database
//    fun loadTasks() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
//
//            try {
//                val tasks = repository.getTasks()
//                _uiState.value = _uiState.value.copy(
//                    tasks = tasks,
//                    isLoading = false
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    errorMessage = "Failed to load tasks: ${e.message}"
//                )
//            }
//        }
//    }
}