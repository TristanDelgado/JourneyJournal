package com.delly.journeyjournal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JourneyEntity
import com.delly.journeyjournal.enums.TransportationMethods
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateJourneyViewModel(
    private val navigateHome: () -> Unit,
    private val navigateToJourney: (String) -> Unit,
    private val repository: JournalRepository,
) : ViewModel() {

    private val _journeyName = MutableStateFlow("")
    val journeyName: StateFlow<String> = _journeyName.asStateFlow()

    private val _journeymanName = MutableStateFlow("")
    val journeymanName: StateFlow<String> = _journeymanName.asStateFlow()

    private val _courseName = MutableStateFlow("")
    val courseName: StateFlow<String> = _courseName.asStateFlow()

    private val _courseRegion = MutableStateFlow("")
    val courseRegion: StateFlow<String> = _courseRegion.asStateFlow()

    private val _description = MutableStateFlow(value = "")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _selectedDate = MutableStateFlow<Long?>(value = null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    private val _selectedTransportationMethod =
        MutableStateFlow<TransportationMethods>(value = TransportationMethods.ON_FOOT)
    val selectedTransportationMethod: StateFlow<TransportationMethods> =
        _selectedTransportationMethod.asStateFlow()

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
            val newJourney = JourneyEntity(
                journeyName = _journeyName.value,
                journeymanName = _journeymanName.value,
                courseName = _courseName.value,
                courseRegion = _courseRegion.value,
                startDate = _selectedDate.value,
                transportationMethod = _selectedTransportationMethod.value,
                description = _description.value
            )

            try {
                repository.insertJourney(journeyEntity = newJourney)
                navigateToJourney(newJourney.journeyName)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}