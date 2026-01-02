package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.enums.DistanceUnit
import com.delly.journeyjournal.enums.TransportationMethods
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Create journey view model
 */
class CreateEditJournalViewModel(
    private val navigateHome: () -> Unit,
    private val createAndNavigateToJournal: (Long) -> Unit,
    private val repository: JournalRepository,
    private val journalToEditId: Long?,
) : ViewModel() {

    // --- Identity ---
    private val _journeyName = MutableStateFlow("")
    val journeyName: StateFlow<String> = _journeyName.asStateFlow()

    private val _journeymanName = MutableStateFlow("")
    val journeymanName: StateFlow<String> = _journeymanName.asStateFlow()

    // --- Course Details ---
    private val _courseName = MutableStateFlow("")
    val courseName: StateFlow<String> = _courseName.asStateFlow()

    private val _courseRegion = MutableStateFlow("")
    val courseRegion: StateFlow<String> = _courseRegion.asStateFlow()

    // --- Description ---
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    // --- Logistics ---
    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    private val _selectedTransportationMethod = MutableStateFlow(TransportationMethods.ON_FOOT)
    val selectedTransportationMethod: StateFlow<TransportationMethods> = _selectedTransportationMethod.asStateFlow()

    private val _selectedDistanceUnit = MutableStateFlow(DistanceUnit.MILES)
    val selectedDistanceUnit: StateFlow<DistanceUnit> = _selectedDistanceUnit.asStateFlow()

    private var _isComplete = false

    init {
        // Edit Mode: Load existing data if ID is provided
        if (journalToEditId != null) {
            viewModelScope.launch {
                val journal = repository.getJournalById(journalToEditId)
                if (journal != null) {
                    _journeyName.value = journal.journalName
                    _journeymanName.value = journal.journeymanName
                    _courseName.value = journal.courseName
                    _courseRegion.value = journal.courseRegion
                    _description.value = journal.description
                    _selectedDate.value = journal.startDate
                    _selectedTransportationMethod.value = journal.transportationMethod
                    _selectedDistanceUnit.value = journal.distanceUnit
                    _isComplete = journal.isComplete
                }
            }
        }
    }

    // --- Update Functions ---

    fun updateJourneyName(newName: String) {
        _journeyName.value = newName
    }

    fun updateJourneymanName(newName: String) {
        _journeymanName.value = newName
    }

    fun updateCourseName(newName: String) {
        _courseName.value = newName
    }

    fun updateCourseRegion(newRegion: String) {
        _courseRegion.value = newRegion
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateSelectedDate(newDate: Long?) {
        _selectedDate.value = newDate
    }

    fun updateTransportationMethod(transportationMethod: TransportationMethods) {
        _selectedTransportationMethod.value = transportationMethod
    }

    fun updateDistanceUnit(distanceUnit: DistanceUnit) {
        _selectedDistanceUnit.value = distanceUnit
    }

    // --- Action Handlers ---

    fun cancelJourney() {
        navigateHome()
    }

    fun saveJourney(onInvalidInput: () -> Unit = {}) {
        viewModelScope.launch {
            // Validation
            if (_journeyName.value.isBlank()) {
                onInvalidInput()
                return@launch
            }

            // Create Entity
            val idToUse = journalToEditId ?: 0
            val newJourney = JournalEntity(
                id = idToUse,
                journalName = _journeyName.value,
                journeymanName = _journeymanName.value,
                courseName = _courseName.value,
                courseRegion = _courseRegion.value,
                startDate = _selectedDate.value,
                transportationMethod = _selectedTransportationMethod.value,
                description = _description.value,
                distanceUnit = _selectedDistanceUnit.value,
                isComplete = _isComplete
            )

            // Database Operations
            if (journalToEditId != null) {
                repository.updateJournal(journalEntity = newJourney)
                createAndNavigateToJournal(newJourney.id)
            } else {
                try {
                    val newId = repository.insertJournal(journalEntity = newJourney)
                    createAndNavigateToJournal(newId)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}