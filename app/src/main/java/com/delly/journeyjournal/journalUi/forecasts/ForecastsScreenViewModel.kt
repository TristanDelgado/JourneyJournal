package com.delly.journeyjournal.journalUi.forecasts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.ForecastEntity
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Represents the UI state for the Forecasts screen.
 *
 * This data class holds all the information required to render the dashboard,
 * including the list of forecasts and summary statistics about the hiker's current progress.
 *
 * @property isLoading True if the data is currently being fetched or calculated.
 * @property forecasts The list of calculated forecast items to display.
 * @property averageMilesPerDay The calculated average daily mileage (excluding zero days), or null if insufficient data.
 * @property lastMileMarker The mile marker of the most recent journal entry, or null if no entries exist.
 */
data class ForecastsUiState(
    val isLoading: Boolean = true,
    val forecasts: List<Forecast> = emptyList(),
    val averageMilesPerDay: Double? = null,
    val lastMileMarker: Double? = null,
)

/**
 * A UI-specific model representing a Forecast/Landmark.
 *
 * Unlike the database entity, this model includes calculated fields for the
 * estimated arrival time based on the user's current hiking pace.
 *
 * @property id The unique identifier of the forecast.
 * @property name The name of the location.
 * @property mileMarker The location on the trail.
 * @property estimatedDaysToArrival The calculated number of days until arrival (null if passed or cannot be calculated).
 * @property estimatedArrivalDate A formatted string (e.g., "Friday, Oct 12") or status text (e.g., "Complete").
 */
data class Forecast(
    val id: Long,
    val name: String,
    val mileMarker: Double,
    val estimatedDaysToArrival: Long?,
    val estimatedArrivalDate: String?,
)

/**
 * ViewModel for managing the Forecasts screen logic.
 *
 * This class is responsible for:
 * 1. Observing changes in the Journal, Entries, and Forecasts tables.
 * 2. Calculating the user's hiking pace (excluding zero days).
 * 3. Predicting arrival dates for future waypoints based on the current date.
 * 4. Handling CRUD operations for forecasts.
 *
 * @property repository The data source for journal data.
 * @property journalId The ID of the current journal being viewed.
 */
class ForecastsScreenViewModel(
    private val repository: JournalRepository,
    private val journalId: Long,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForecastsUiState(isLoading = true))
    val uiState: StateFlow<ForecastsUiState> = _uiState.asStateFlow()

    init {
        observeForecasts()
    }

    /**
     * Observes the database for changes and recalculates forecasts in real-time.
     *
     * This function performs the following logic whenever the data changes:
     * 1. Sorts entries by date to determine the most recent mile marker.
     * 2. Calculates the average miles per day based on actual hiking days.
     * 3. Iterates through all forecasts:
     * - If the forecast mile marker is less than the current location, it marks it as "Complete".
     * - If ahead, it calculates the distance remaining and divides by the average pace.
     * - Adds the estimated days to the *current system time* to predict a calendar date.
     *
     * Calculations are offloaded to [Dispatchers.Default] to prevent UI jank.
     */
    private fun observeForecasts() {
        viewModelScope.launch {
            repository.getJournalWithEntriesAndForecasts(journalId)
                .collectLatest { journalWithEntriesAndForecasts ->

                    if (journalWithEntriesAndForecasts == null || journalWithEntriesAndForecasts.forecasts.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                forecasts = emptyList(),
                                averageMilesPerDay = null,
                                lastMileMarker = null,
                            )
                        }
                        return@collectLatest
                    }

                    val sortedEntries = withContext(Dispatchers.Default) {
                        journalWithEntriesAndForecasts.entries.sortedBy { it.date }
                    }

                    if (sortedEntries.isEmpty()) {
                        val basicForecasts = journalWithEntriesAndForecasts.forecasts.map {
                            Forecast(
                                it.id,
                                it.name,
                                it.mileMarker,
                                null,
                                null
                            )
                        }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                forecasts = basicForecasts,
                                averageMilesPerDay = null,
                                lastMileMarker = null,
                            )
                        }
                        return@collectLatest
                    }

                    val avgMilesPerDay = calculateAverageMilesPerDay(sortedEntries)
                    val lastEntry = sortedEntries.last()
                    val lastMileMarker = lastEntry.endMileMarker.toDoubleOrNull() ?: 0.0

                    val calculatedForecasts = withContext(Dispatchers.Default) {
                        // 1. Define formatter and current time ONCE outside the loop for efficiency
                        val dateFormatter = java.text.SimpleDateFormat("EEEE, MMMM d", java.util.Locale.getDefault())
                        val currentTimestamp = System.currentTimeMillis()

                        journalWithEntriesAndForecasts.forecasts.map { forecast ->

                            // 2. Check if we have already passed this marker
                            if (lastMileMarker >= forecast.mileMarker) {
                                // CASE A: The location is behind us
                                Forecast(
                                    id = forecast.id,
                                    name = forecast.name,
                                    mileMarker = forecast.mileMarker,
                                    estimatedDaysToArrival = null,
                                    estimatedArrivalDate = "Complete"
                                )
                            } else {
                                // CASE B: The location is ahead of us
                                val remainingMiles = forecast.mileMarker - lastMileMarker

                                val estimatedDays = if (avgMilesPerDay > 0) {
                                    (remainingMiles / avgMilesPerDay).toLong()
                                } else null

                                val estimatedDate = if (estimatedDays != null) {
                                    // CHANGE: Use currentTimestamp instead of lastEntry.date
                                    val futureTime = currentTimestamp + TimeUnit.DAYS.toMillis(estimatedDays)
                                    dateFormatter.format(Date(futureTime))
                                } else null

                                Forecast(
                                    id = forecast.id,
                                    name = forecast.name,
                                    mileMarker = forecast.mileMarker,
                                    estimatedDaysToArrival = estimatedDays,
                                    estimatedArrivalDate = estimatedDate
                                )
                            }
                        }
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            forecasts = calculatedForecasts,
                            averageMilesPerDay = avgMilesPerDay,
                            lastMileMarker = lastMileMarker,
                        )
                    }
                }
        }
    }

    /**
     * Calculates the average daily mileage based only on days where hiking occurred.
     *
     * This method filters out "Zero Days" (days where distance is 0.0) to provide
     * a more accurate representation of the user's actual hiking speed.
     *
     * @param entries The list of all journal entries.
     * @return The average miles per hiking day, or 0.0 if no hiking days exist.
     */
    private fun calculateAverageMilesPerDay(entries: List<JournalEntryEntity>): Double {
        // 1. Convert strings to doubles and keep only those greater than 0
        val hikingDays = entries.mapNotNull { it.distanceHiked.toDoubleOrNull() }
            .filter { it > 0.0 }

        // 2. Safety check to avoid division by zero (if all days were Zero days)
        if (hikingDays.isEmpty()) return 0.0

        // 3. Calculate average based only on days walked
        return hikingDays.average()
    }

    /**
     * Adds a new forecast to the database.
     *
     * @param name The name of the location.
     * @param mileMarker The mile marker location.
     */
    fun addForecast(
        name: String,
        mileMarker: Double,
    ) {
        viewModelScope.launch {
            val forecast = ForecastEntity(
                journalId = journalId,
                name = name,
                mileMarker = mileMarker
            )
            repository.insertForecast(forecast)
        }
    }

    /**
     * Deletes a specific forecast from the database.
     *
     * @param id The ID of the forecast to delete.
     */
    fun deleteForecast(id: Long) {
        viewModelScope.launch {
            repository.deleteForecast(id)
        }
    }

    /**
     * Updates an existing forecast in the database.
     *
     * @param id The ID of the forecast to update.
     * @param name The new name.
     * @param mileMarker The new mile marker.
     */
    fun updateForecast(
        id: Long,
        name: String,
        mileMarker: Double,
    ) {
        viewModelScope.launch {
            repository.updateForecast(
                id,
                name,
                mileMarker
            )
        }
    }
}