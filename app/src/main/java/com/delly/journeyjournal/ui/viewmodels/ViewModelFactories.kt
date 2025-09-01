package com.delly.journeyjournal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.delly.journeyjournal.db.JournalRepository

class CreateJourneyViewModelFactory(private val navController: NavController, private val repository: JournalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateJourneyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateJourneyViewModel(navController = navController, repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}