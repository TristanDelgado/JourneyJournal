package com.delly.journeyjournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.enums.TransportationMethods
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Create journey view model
 *
 * @property navigateHome Cancel creating a new journey
 * @property createAndNavigateToJournal Navigate to the journal after creating it
 * @property repository The repository to store the journal in
 * @constructor Create empty Create journey view model
 */
class CreateEditJournalViewModel(
    private val navigateHome: () -> Unit,
    private val createAndNavigateToJournal: (Int) -> Unit,
    private val repository: JournalRepository,
    private val journalToEditId: Int?,
) : ViewModel() {

    private val _journeyName = MutableStateFlow("")
    val journeyName: StateFlow<String> = _journeyName.asStateFlow()

    private val _journeymanName = MutableStateFlow("")
    val journeymanName: StateFlow<String> = _journeymanName.asStateFlow()

    private val _courseName = MutableStateFlow("")
    val courseName: StateFlow<String> = _courseName.asStateFlow()

    private val _courseRegion = MutableStateFlow("")
    val courseRegion: StateFlow<String> = _courseRegion.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    private val _selectedTransportationMethod =
        MutableStateFlow(TransportationMethods.ON_FOOT)
    val selectedTransportationMethod: StateFlow<TransportationMethods> =
        _selectedTransportationMethod.asStateFlow()

    init {
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
                }
            }
        }
    }

    // -----------------------------------------
    // Functions to update the state from the UI
    // -----------------------------------------
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

    // Cancel the creation of a new journey
    fun cancelJourney() {
        navigateHome()
    }

    // Adds the new journey to the database
    // and navigates to the journey view
    fun saveJourney() {
        viewModelScope.launch {
            // Create a Journey Entity
            // id is 0 by default which means auto-generate for new entries
            // if we are editing, we need to use the existing id
            val idToUse = journalToEditId ?: 0
            val newJourney = JournalEntity(
                id = idToUse,
                journalName = _journeyName.value,
                journeymanName = _journeymanName.value,
                courseName = _courseName.value,
                courseRegion = _courseRegion.value,
                startDate = _selectedDate.value,
                transportationMethod = _selectedTransportationMethod.value,
                description = _description.value
            )

            // Check if we are editing an existing journey or creating a new one
            // We assume we are editing if journalToEditId was provided in constructor
            if (journalToEditId != null) {
                repository.updateJournal(journalEntity = newJourney)
                createAndNavigateToJournal(newJourney.id)
            } else {
                try {
                    val newId = repository.insertJournal(journalEntity = newJourney)
                    createAndNavigateToJournal(newId.toInt())
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}
