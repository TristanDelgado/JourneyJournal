package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class CreateEntryViewModel(
    private val navigateBack: () -> Unit,
    private val repository: JournalRepository,
    private val journalId: Int,
) : ViewModel() {

    // --- Date & Day ---
    private val _entryDate = MutableStateFlow(value = System.currentTimeMillis())
    val entryDate: StateFlow<Long> = _entryDate

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    private val _dayNumber = MutableStateFlow("")
    val dayNumber: StateFlow<String> = _dayNumber

    // Mocking a previous day for the UI hint
    private val _previousDayNumber = MutableStateFlow("0")
    val previousDayNumber: StateFlow<String> = _previousDayNumber

    // --- Locations ---
    private val _startLocation = MutableStateFlow("")
    val startLocation: StateFlow<String> = _startLocation

    private val _endLocation = MutableStateFlow("")
    val endLocation: StateFlow<String> = _endLocation

    // --- Mile Markers & Distance ---
    private val _startMileMarker = MutableStateFlow("")
    val startMileMarker: StateFlow<String> = _startMileMarker

    private val _endMileMarker = MutableStateFlow("")
    val endMileMarker: StateFlow<String> = _endMileMarker

    private val _distanceHiked = MutableStateFlow("")
    val distanceHiked: StateFlow<String> = _distanceHiked

    // --- Elevation ---
    // Assuming "Total Ascent" fields meant Start/End Elevation to calculate net change
    // based on your request to "subtract" them.
    private val _elevationStart = MutableStateFlow("")
    val elevationStart: StateFlow<String> = _elevationStart

    private val _elevationEnd = MutableStateFlow("")
    val elevationEnd: StateFlow<String> = _elevationEnd

    private val _netElevationChange = MutableStateFlow("")
    val netElevationChange: StateFlow<String> = _netElevationChange

    // --- Toggles ---
    private val _sleptInBed = MutableStateFlow(false)
    val sleptInBed: StateFlow<Boolean> = _sleptInBed

    private val _tookShower = MutableStateFlow(false)
    val tookShower: StateFlow<Boolean> = _tookShower

    // --- Details ---
    private val _trailConditions = MutableStateFlow("")
    val trailConditions: StateFlow<String> = _trailConditions

    private val _weather = MutableStateFlow("")
    val weather: StateFlow<String> = _weather

    private val _wildlifeSightings = MutableStateFlow("")
    val wildlifeSightings: StateFlow<String> = _wildlifeSightings

    private val _resupplyNotes = MutableStateFlow("")
    val resupplyNotes: StateFlow<String> = _resupplyNotes

    // --- Ratings ---
    private val _dayRating = MutableStateFlow("")
    val dayRating: StateFlow<String> = _dayRating

    private val _moodRating = MutableStateFlow("")
    val moodRating: StateFlow<String> = _moodRating

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes

    init {
        // Todo: Fetch the actual previous day number from repository here
        _previousDayNumber.value = "14" // Placeholder example
    }

    // --- Update Functions ---

    fun updateSelectedDate(newDate: Long?) {
        _selectedDate.value = newDate
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

    // Distance Calculation Logic
    fun updateStartMileMarker(marker: String) {
        if (isValidNumber(marker)) {
            _startMileMarker.value = marker
            calculateDistance()
        }
    }

    fun updateEndMileMarker(marker: String) {
        if (isValidNumber(marker)) {
            _endMileMarker.value = marker
            calculateDistance()
        }
    }

    private fun calculateDistance() {
        val start = _startMileMarker.value.toDoubleOrNull()
        val end = _endMileMarker.value.toDoubleOrNull()
        if (start != null && end != null) {
            val dist = abs(start - end)
            // Round to 1 decimal place
            _distanceHiked.value = String.format(
                "%.1f",
                dist
            )
        } else {
            _distanceHiked.value = ""
        }
    }

    // Elevation Calculation Logic
    fun updateElevationStart(elev: String) {
        if (isValidNumber(elev)) {
            _elevationStart.value = elev
            calculateElevationChange()
        }
    }

    fun updateElevationEnd(elev: String) {
        if (isValidNumber(elev)) {
            _elevationEnd.value = elev
            calculateElevationChange()
        }
    }

    private fun calculateElevationChange() {
        val start = _elevationStart.value.toIntOrNull()
        val end = _elevationEnd.value.toIntOrNull()
        if (start != null && end != null) {
            val diff = end - start
            _netElevationChange.value = diff.toString()
        } else {
            _netElevationChange.value = ""
        }
    }

    // Toggles
    fun toggleSleptInBed(subbed: Boolean) {
        _sleptInBed.value = subbed
    }

    fun toggleTookShower(showered: Boolean) {
        _tookShower.value = showered
    }

    // Dropdowns
    fun updateTrailConditions(condition: String) {
        _trailConditions.value = condition
    }

    fun updateWeather(newWeather: String) {
        _weather.value = newWeather
    }

    fun updateDayRating(rating: String) {
        _dayRating.value = rating
    }

    fun updateMoodRating(rating: String) {
        _moodRating.value = rating
    }

    // Text Fields
    fun updateWildlifeSightings(newSightings: String) {
        _wildlifeSightings.value = newSightings
    }

    fun updateResupplyNotes(newNotes: String) {
        _resupplyNotes.value = newNotes
    }

    fun updateNotes(newNotes: String) {
        _notes.value = newNotes
    }

    private fun isValidNumber(input: String): Boolean {
        return input.isEmpty() || input.matches(Regex("^-?[0-9]*\\.?[0-9]*$"))
    }

    // --- Button Click Handlers ---
    fun saveEntry(onInvalidInput: () -> Unit) {
        viewModelScope.launch {
            // 1. Validation Logic
            if (_dayNumber.value.isBlank() || (_selectedDate.value == null && _entryDate.value == 0L)) {
                onInvalidInput() // Trigger UI callback for error
                return@launch
            }

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
                notes = _notes.value,
                startMileMarker = _startMileMarker.value,
                endMileMarker = _endMileMarker.value,
                elevationStart = _elevationStart.value,
                elevationEnd = _elevationEnd.value,
                netElevationChange = _netElevationChange.value,
                sleptInBed = _sleptInBed.value,
                tookShower = _tookShower.value,
                weather = _weather.value,
                dayRating = _dayRating.value,
                moodRating = _moodRating.value
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
