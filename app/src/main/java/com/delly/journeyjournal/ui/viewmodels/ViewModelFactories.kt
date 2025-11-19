package com.delly.journeyjournal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delly.journeyjournal.db.JournalRepository

class CreateJourneyViewModelFactory(
    private val navigateHome: () -> Unit,
    private val navigateToJourney: (String) -> Unit,
    private val repository: JournalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateJourneyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateJourneyViewModel(
                navigateHome = navigateHome,
                navigateToJourney = navigateToJourney,
                repository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CreateEntryViewModelFactory(
    private val navigateBack: () -> Unit,
    private val repository: JournalRepository,
    private val journalName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateEntryViewModel(navigateBack, repository, journalName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}