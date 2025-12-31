package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delly.journeyjournal.db.JournalRepository

class CreateJournalViewModelFactory(
    private val navigateHome: () -> Unit,
    private val navigateToJourney: (Int) -> Unit,
    private val repository: JournalRepository,
    private val journalToEditId: Int?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditJournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateEditJournalViewModel(
                navigateHome = navigateHome,
                createAndNavigateToJournal = navigateToJourney,
                repository = repository,
                journalToEditId = journalToEditId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CreateEntryViewModelFactory(
    private val navigateBack: () -> Unit,
    private val repository: JournalRepository,
    private val journalId: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateEntryViewModel(
                navigateBack,
                repository,
                journalId,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class JournalEntriesViewModelFactory(
    private val repository: JournalRepository,
    private val journalId: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalEntriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalEntriesViewModel(
                repository,
                journalId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class JournalViewViewModelFactory(
    private val repository: JournalRepository,
    private val currentJournalId: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewViewModel(
                repository,
                currentJournalId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}