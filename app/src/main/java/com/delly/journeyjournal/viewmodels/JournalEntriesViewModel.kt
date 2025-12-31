package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalWithEntries
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Journal entries view model
 *
 * @property repository The repository to get journal data from
 * @property journalId The ID of our active journal
 * @constructor Create empty Journey entries view model
 */
class JournalEntriesViewModel(
    private val repository: JournalRepository,
    private val journalId: Int,
) : ViewModel() {

    /**
     * Observes the database for the specific journal and its entries.
     *
     * Uses [stateIn] to convert the Flow from Room into a StateFlow
     * that Compose can safely observe.
     */
    val journalWithEntries: StateFlow<JournalWithEntries?> = repository
        .getJournalWithEntries(journalId)
        .stateIn(
            scope = viewModelScope,
            // WhileSubscribed(5000) keeps the flow active for 5 seconds after
            // the UI stops observing (useful for configuration changes/rotations)
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
