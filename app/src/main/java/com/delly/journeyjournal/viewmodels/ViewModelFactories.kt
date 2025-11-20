package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delly.journeyjournal.db.JournalRepository

class CreateJourneyViewModelFactory(
    private val navigateHome: () -> Unit,
    private val navigateToJourney: (String) -> Unit,
    private val repository: JournalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateJournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateJournalViewModel(
                navigateHome = navigateHome,
                createAndNavigateToJournal = navigateToJourney,
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

class JourneyEntriesViewModelFactory(
    private val repository: JournalRepository,
    private val journeyName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneyEntriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JourneyEntriesViewModel(repository, journeyName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}