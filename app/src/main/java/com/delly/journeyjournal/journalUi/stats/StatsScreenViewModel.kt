package com.delly.journeyjournal.journalUi.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Represents the UI state for the Statistics screen.
 *
 * This data class holds all the calculated metrics in a pre-formatted String format
 * ready for display in the UI. It also tracks the loading state of the data.
 *
 * @property isLoading True if the data is currently being fetched or calculated.
 * @property totalEntries The total number of journal entries found.
 * @property avgMilesPerDay Average daily mileage including "Zero" (rest) days.
 * @property avgMilesNoZeros Average daily mileage excluding days where 0 miles were hiked.
 * @property maxDaysWithoutShower The longest streak of consecutive days without a shower record.
 * // ... (other properties are self-explanatory based on names)
 */
data class StatsUiState(
    val isLoading: Boolean = true,
    val totalEntries: Int = 0,
    val avgMilesPerDay: String = "--",
    val avgMilesNoZeros: String = "--",
    val avgMilesPerWeek: String = "--",
    val highestMileageDay: String = "--",
    val totalNetAscent: String = "--",
    val totalNetDescent: String = "--",
    val biggestAscentDay: String = "--",
    val biggestDescentDay: String = "--",
    val totalZeros: Int = 0,
    val daysSinceLastZero: Int = 0,
    val daysOnGround: Int = 0,
    val daysInBed: Int = 0,
    val totalShowers: Int = 0,
    val daysSinceLastShower: Int = 0,
    val maxDaysWithoutShower: Int = 0,
)

/**
 * The ViewModel responsible for calculating and exposing statistics for a specific journal.
 *
 * This ViewModel observes the database for changes to the specific journal's entries.
 * When changes are detected, it recalculates various statistics (Distance, Elevation,
 * Hygiene, etc.) on a background thread to ensure UI responsiveness.
 *
 * @property repository The data source for journal entries.
 * @property journalId The ID of the journal to analyze.
 */
class StatsScreenViewModel(
    private val repository: JournalRepository,
    private val journalId: Long,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        observeStats()
    }

    /**
     * Begins observing the flow of journal entries from the repository.
     *
     * Uses [collectLatest] to ensure that if rapid database updates occur,
     * intermediate calculations are cancelled and only the latest data is processed.
     * Calculations are offloaded to [Dispatchers.Default] as they are CPU-intensive.
     */
    private fun observeStats() {
        viewModelScope.launch {
            // We use collectLatest so if the DB emits 3 updates rapidly,
            // we cancel the calculation of the old ones and only calculate the newest.
            // Assumption: repository.getJournalWithEntries returns Flow<JournalWithEntries?>
            repository.getJournalWithEntries(journalId).collectLatest { journalWithEntries ->

                if (journalWithEntries == null || journalWithEntries.entries.isEmpty()) {
                    _uiState.value = StatsUiState(isLoading = false)
                } else {
                    // Offload the heavy calculation/sorting to the Default (CPU) dispatcher
                    // This ensures the UI thread doesn't stutter during the math
                    val stats = withContext(Dispatchers.Default) {
                        val sortedEntries = journalWithEntries.entries.sortedBy { it.date }
                        processEntries(sortedEntries)
                    }
                    _uiState.value = stats
                }
            }
        }
    }

    /**
     * Processes a list of raw [JournalEntryEntity] objects into a [StatsUiState].
     *
     * This function performs several heavy operations:
     * 1. Parses String fields (distance, elevation) into numeric types.
     * 2. Calculates aggregates (sums, averages, min/max).
     * 3. Calculates streaks (days since last shower, days on ground).
     * 4. Formats the results into display-ready Strings.
     *
     * @param entries The list of journal entries to process, expected to be sorted by date.
     * @return A fully populated [StatsUiState] object.
     */
    private fun processEntries(entries: List<JournalEntryEntity>): StatsUiState {
        // --- 1. Basic Parsing ---
        fun parseDouble(str: String): Double {
            val cleanStr = str.replace(Regex("[^0-9.]"), "")
            return cleanStr.toDoubleOrNull() ?: 0.0
        }

        fun parseInt(str: String): Int {
            val cleanStr = str.replace(Regex("[^0-9-]"), "")
            return cleanStr.toIntOrNull() ?: 0
        }

        val distances = entries.map { parseDouble(it.distanceHiked) }
        val elevationChanges = entries.map { parseInt(it.netElevationChange) }

        // --- 2. Distance Stats ---
        val totalMiles = distances.sum()
        val totalDays = entries.size

        val avgMilesPerDay = if (totalDays > 0) totalMiles / totalDays else 0.0

        val zeroDaysCount = distances.count { it == 0.0 }
        val hikingDaysCount = totalDays - zeroDaysCount
        val avgMilesNoZeros = if (hikingDaysCount > 0) totalMiles / hikingDaysCount else 0.0

        val weeks = totalDays / 7.0
        val avgMilesPerWeek = if (weeks >= 1.0) totalMiles / weeks else totalMiles

        val highestMileage = distances.maxOrNull() ?: 0.0

        // --- 3. Elevation Stats ---
        val totalNetAscent = elevationChanges.filter { it > 0 }.sum()
        val totalNetDescent = elevationChanges.filter { it < 0 }.sum()
        val biggestAscent = elevationChanges.maxOrNull() ?: 0
        val biggestDescent = elevationChanges.minOrNull() ?: 0


        // --- 4. Amenities Stats ---
        val daysInBed = entries.count { it.sleptInBed }
        val daysOnGround = totalDays - daysInBed
        val totalShowers = entries.count { it.tookShower }

        // --- 5. Streak Calculations ---
        val reversedEntries = entries.reversed()

        var daysSinceZero = 0
        for (entry in reversedEntries) {
            if (parseDouble(entry.distanceHiked) == 0.0) break
            daysSinceZero++
        }

        var daysSinceShower = 0
        for (entry in reversedEntries) {
            if (entry.tookShower) break
            daysSinceShower++
        }

        var maxShowerGap = 0
        var currentShowerGap = 0
        for (entry in entries) {
            if (entry.tookShower) {
                if (currentShowerGap > maxShowerGap) maxShowerGap = currentShowerGap
                currentShowerGap = 0
            } else {
                currentShowerGap++
            }
        }
        if (currentShowerGap > maxShowerGap) maxShowerGap = currentShowerGap

        // --- 6. Formatting ---
        fun formatDouble(d: Double): String = "%.1f".format(d)

        return StatsUiState(
            isLoading = false,
            totalEntries = totalDays,
            avgMilesPerDay = formatDouble(avgMilesPerDay),
            avgMilesNoZeros = formatDouble(avgMilesNoZeros),
            avgMilesPerWeek = formatDouble(avgMilesPerWeek),
            highestMileageDay = formatDouble(highestMileage),
            totalNetAscent = "+$totalNetAscent",
            totalNetDescent = "$totalNetDescent",
            biggestAscentDay = "+$biggestAscent",
            biggestDescentDay = "$biggestDescent",
            totalZeros = zeroDaysCount,
            daysSinceLastZero = daysSinceZero,
            daysOnGround = daysOnGround,
            daysInBed = daysInBed,
            totalShowers = totalShowers,
            daysSinceLastShower = daysSinceShower,
            maxDaysWithoutShower = maxShowerGap
        )
    }
}