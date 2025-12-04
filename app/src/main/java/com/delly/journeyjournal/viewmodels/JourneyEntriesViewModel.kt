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
 * Journey entries view model
 *
 * @property repository
 * @property journeyId
 * @constructor Create empty Journey entries view model
 */
class JourneyEntriesViewModel(
    private val repository: JournalRepository,
    private val journeyId: Int
) : ViewModel() {

    /**
     * _journey with entries
     */
    private val _journeyWithEntries = MutableStateFlow<JournalWithEntries?>(null)
    val journeyWithEntries: StateFlow<JournalWithEntries?> = _journeyWithEntries.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            _journeyWithEntries.value = repository.getJourneyWithEntries(journeyId)
        }
    }
}
