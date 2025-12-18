package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Create entry view model
 *
 * @property navigateBack Call back to cancel creating a new entry in the current journal
 * @property repository The repository to store the entry in
 * @property journalId The journal id to associate the entry with
 * @constructor Create empty "Create entry view model"
 */
class CreateEntryViewModel(
    private val navigateBack: () -> Unit,
    private val repository: JournalRepository,
    private val journalId: Int,
) : ViewModel() {

    // --- StateFlows for all fields ---
    private val _entryDate = MutableStateFlow(value = System.currentTimeMillis())
    val entryDate: StateFlow<Long> = _entryDate

    private val _dayNumber = MutableStateFlow("")
    val dayNumber: StateFlow<String> = _dayNumber

    private val _startLocation = MutableStateFlow("")
    val startLocation: StateFlow<String> = _startLocation

    private val _endLocation = MutableStateFlow("")
    val endLocation: StateFlow<String> = _endLocation

    private val _distanceHiked = MutableStateFlow("")
    val distanceHiked: StateFlow<String> = _distanceHiked

    private val _weather = MutableStateFlow("")
    val weather: StateFlow<String> = _weather

    private val _trailConditions = MutableStateFlow("")
    val trailConditions: StateFlow<String> = _trailConditions

    private val _wildlifeSightings = MutableStateFlow("")
    val wildlifeSightings: StateFlow<String> = _wildlifeSightings

    private val _resupplyNotes = MutableStateFlow("")
    val resupplyNotes: StateFlow<String> = _resupplyNotes

    private val _physicalMentalState = MutableStateFlow("") // e.g., "1" to "5"
    val physicalMentalState: StateFlow<String> = _physicalMentalState

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    // TODO: Add state for photos/videos
    // private val _photos = MutableStateFlow<List<Uri>>(emptyList())
    // val photos: StateFlow<List<Uri>> = _photos

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes

    // --- Update Functions ---
    fun updateEntryDate(newDate: Long) {
        _entryDate.value = newDate
    }

    // TODO: Add weather update function
    fun updateWeather(newWeather: String) {
        _weather.value = newWeather
    }

    fun updateDayNumber(newDay: String) {
        _dayNumber.value = newDay
    }

    fun updateStartLocation(newLocation: String) {
        _startLocation.value = newLocation
    }

    fun updateEndLocation(newLocation: String) {
        _endLocation.value = newLocation
    }

    fun updateDistanceHiked(newDistance: String) {
        _distanceHiked.value = newDistance
    }

    fun updateTrailConditions(newConditions: String) {
        _trailConditions.value = newConditions
    }

    fun updateWildlifeSightings(newSightings: String) {
        _wildlifeSightings.value = newSightings
    }

    fun updateResupplyNotes(newNotes: String) {
        _resupplyNotes.value = newNotes
    }

    fun updateNotes(newNotes: String) {
        _notes.value = newNotes
    }

    fun updateSelectedDate(newDate: Long?) {
        _selectedDate.value = newDate
    }
    // TODO: Add update functions for Date, Weather, Rating, and Photos

    // --- Button Click Handlers ---
    fun saveEntry() {
        viewModelScope.launch {
            // TODO: Validate input

            val newEntry = JournalEntryEntity(
                ownerId = journalId,
                date = _selectedDate.value ?: _entryDate.value,
                dayNumber = _dayNumber.value,
                startLocation = _startLocation.value,
                endLocation = _endLocation.value,
                distanceHiked = _distanceHiked.value,
                trailConditions = _trailConditions.value,
                wildlifeSightings = _wildlifeSightings.value,
                resupplyNotes = _resupplyNotes.value,
                notes = _notes.value
            )

            try {
                repository.insertJourneyEntry(entry = newEntry)
                navigateBack()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun cancelEntry() {
        navigateBack()
    }
}
