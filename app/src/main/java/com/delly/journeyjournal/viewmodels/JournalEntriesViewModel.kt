package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalWithEntries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Journal entries view model
 *
 * @property repository
 * @property journalId
 * @constructor Create empty Journey entries view model
 */
class JournalEntriesViewModel(
    private val repository: JournalRepository,
    private val journalId: Int
) : ViewModel() {

    /**
     * _journal with entries
     */
    private val _journalWithEntries = MutableStateFlow<JournalWithEntries?>(null)
    val journalWithEntries: StateFlow<JournalWithEntries?> = _journalWithEntries.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            _journalWithEntries.value = repository.getJourneyWithEntries(journalId)
        }
    }
}
