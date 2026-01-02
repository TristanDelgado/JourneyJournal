package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JournalViewViewModel(
    private val repository: JournalRepository,
    private val journalId: Long
) : ViewModel() {

    // Internal mutable state
    // Initialize with null to represent the loading state
    private var _currentJournal = MutableStateFlow<JournalEntity?>(null)
    val currentJournal: StateFlow<JournalEntity?> = _currentJournal.asStateFlow()

    private val _titleOfPage = MutableStateFlow("Journey")
    val titleOfPage: StateFlow<String> = _titleOfPage.asStateFlow()

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int> = _selectedIndex.asStateFlow()

    init {
        loadJournal()
    }

    private fun loadJournal() {
        viewModelScope.launch {
            // Fetch the journal from the repository off the main thread
            val journal = repository.getJournalById(journalId)
            _currentJournal.value = journal

            // If the journal was found, update the title
            journal?.let {
                _titleOfPage.value = it.journalName.ifBlank { "Untitled Journal" }
            }
        }
    }

    fun invertCompleteStatus() {
        val current = _currentJournal.value ?: return
        val updatedJournal = current.copy(isComplete = !current.isComplete)
        _currentJournal.value = updatedJournal

        viewModelScope.launch {
            repository.updateJournal(journalEntity = updatedJournal)
        }
    }

    fun updateTitle(newTitle: String) {
        _titleOfPage.value = newTitle
    }

    fun updateSelectedIndex(index: Int) {
        _selectedIndex.value = index
    }
}
