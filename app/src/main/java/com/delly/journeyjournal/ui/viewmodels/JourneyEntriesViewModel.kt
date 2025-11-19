package com.delly.journeyjournal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JourneyEntryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JourneyEntriesViewModel(
    private val repository: JournalRepository,
    private val journeyName: String
) : ViewModel() {

    private val _entryList = MutableStateFlow<List<JourneyEntryEntity>>(value = emptyList())
    val entryList: StateFlow<List<JourneyEntryEntity>> = _entryList.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            try {
                // Get the journey object to find the list of entry IDs (keys)
                val journey = repository.getJourneyByName(journeyName)
                val entryKeys = journey?.entryKeys

                if (!entryKeys.isNullOrEmpty()) {
                    val entries = repository.getEntriesByIds(entryKeys)
                    _entryList.value = entries
                } else {
                    val exampleEntry = JourneyEntryEntity(
                        id = 0,
                        dayNumber = "1",
                        startLocation = "Valley",
                        endLocation = "Mountain",
                        distanceHiked = "2",
                        trailConditions = "Rough",
                        wildlifeSightings = "None",
                        resupplyNotes = "None",
                        notes = "Example"
                    )
                    _entryList.value = listOf(exampleEntry)
                }
            } catch (e: Exception) {
                // TODO: Add exception handling here
            }
        }
    }
}
