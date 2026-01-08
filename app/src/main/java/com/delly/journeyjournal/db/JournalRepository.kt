package com.delly.journeyjournal.db

import com.delly.journeyjournal.db.dataAccessObjects.ForecastDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntryEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesAndForecastsDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesDao
import com.delly.journeyjournal.db.entities.ForecastEntity
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.db.entities.JournalWithEntries
import com.delly.journeyjournal.db.entities.JournalWithEntriesAndForecasts
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to multiple data sources.
 *
 * It acts as a single source of truth and a mediator between the domain/UI layer
 * and the underlying data layer (Room DAOs). It handles data operations for Journals,
 * Entries, Forecasts, and their composite relationships.
 *
 * @property journalEntityDao DAO for [JournalEntity] operations (CRUD).
 * @property journalEntryEntityDao DAO for [JournalEntryEntity] operations.
 * @property journalWithEntriesDao DAO for joining Journals with their Entries.
 * @property forecastDao DAO for [ForecastEntity] operations.
 * @property journalWithEntriesAndForecastsDao DAO for joining Journals, Entries, and Forecasts.
 */
class JournalRepository(
    private val journalEntityDao: JournalEntityDao,
    private val journalEntryEntityDao: JournalEntryEntityDao,
    private val journalWithEntriesDao: JournalWithEntriesDao,
    private val forecastDao: ForecastDao,
    private val journalWithEntriesAndForecastsDao: JournalWithEntriesAndForecastsDao,
) {

    // --------------------------------------------------------------------------------
    // Region: Journal Operations
    // --------------------------------------------------------------------------------
    /**
     * Retrieves a specific journal by its unique identifier.
     *
     * @param id The primary key of the journal.
     * @return The [JournalEntity] if found, or null if it does not exist.
     */
    suspend fun getJournalById(id: Long): JournalEntity? =
        journalEntityDao.getJournalById(journalId = id)

    /**
     * Inserts a new journal into the database.
     *
     * @param journalEntity The [JournalEntity] to insert.
     * @return The row ID of the newly inserted journal.
     */
    suspend fun insertJournal(journalEntity: JournalEntity): Long =
        journalEntityDao.insertJournal(journalEntity)

    /**
     * Updates an existing journal's details.
     *
     * @param journalEntity The [JournalEntity] with updated values.
     */
    suspend fun updateJournal(journalEntity: JournalEntity) =
        journalEntityDao.updateJournal(journalEntity)

    /**
     * Deletes a specific journal from the database.
     *
     * Note: This will likely trigger a cascade delete of associated entries and forecasts
     * if the database foreign keys are configured with CASCADE.
     *
     * @param journalEntity The [JournalEntity] to delete.
     */
    fun deleteJournal(journalEntity: JournalEntity) =
        journalEntityDao.deleteJournal(journalEntity)

    // --------------------------------------------------------------------------------
    // Region: Entry Operations
    // --------------------------------------------------------------------------------

    /**
     * Retrieves a specific journal entry by its unique identifier.
     *
     * @param id The primary key of the entry.
     * @return The [JournalEntryEntity] if found, null otherwise.
     */
    suspend fun getEntryById(id: Long): JournalEntryEntity? =
        journalEntryEntityDao.getEntryById(id = id)

    /**
     * Retrieves the most recently created entry for a specific journal.
     * Useful for calculating the next day number or resuming a hike.
     *
     * @param journalId The foreign key ID of the journal.
     * @return The last [JournalEntryEntity] added, or null if the journal is empty.
     */
    fun getLastEntryForJournal(journalId: Long): JournalEntryEntity? =
        journalEntryEntityDao.getLastEntryForJournal(journalId)

    /**
     * Inserts a new entry into a journal.
     *
     * @param entry The [JournalEntryEntity] to insert.
     * @return The row ID of the inserted entry.
     */
    suspend fun insertJournalEntry(entry: JournalEntryEntity): Long =
        journalEntryEntityDao.insertJournalEntry(entry)

    /**
     * Updates an existing journal entry with new values.
     *
     * @param entry The [JournalEntryEntity] containing the updated data.
     */
    suspend fun updateEntry(entry: JournalEntryEntity) =
        journalEntryEntityDao.updateEntry(entry)

    /**
     * Deletes a specific entry from a journal.
     *
     * @param entry The [JournalEntryEntity] to delete.
     */
    suspend fun deleteEntry(entry: JournalEntryEntity) =
        journalEntryEntityDao.deleteEntry(entry)

    // --------------------------------------------------------------------------------
    // Region: Forecast Operations
    // --------------------------------------------------------------------------------

    /**
     * Inserts a new forecast/waypoint for a journal.
     *
     * @param forecast The [ForecastEntity] to insert.
     */
    suspend fun insertForecast(forecast: ForecastEntity) {
        forecastDao.insertForecast(forecast)
    }

    /**
     * Deletes a specific forecast by its ID.
     *
     * @param id The primary key of the forecast to delete.
     */
    suspend fun deleteForecast(id: Long) {
        forecastDao.deleteForecast(id)
    }

    /**
     * Updates the editable fields of a forecast.
     *
     * @param id The primary key of the forecast to update.
     * @param name The new name for the forecast location.
     * @param mileMarker The new mile marker for the forecast location.
     */
    suspend fun updateForecast(
        id: Long,
        name: String,
        mileMarker: Double,
    ) {
        forecastDao.updateForecast(
            id,
            name,
            mileMarker
        )
    }

    // --------------------------------------------------------------------------------
    // Region: Composite Data Operations (Joins)
    // --------------------------------------------------------------------------------

    /**
     * Retrieves a journal combined with a list of all its entries.
     *
     * @param journalId The id of the journal.
     * @return A [Flow] emitting a [JournalWithEntries] object, or null if the journal doesn't exist.
     */
    fun getJournalWithEntries(journalId: Long): Flow<JournalWithEntries?> =
        journalWithEntriesDao.getJournalWithEntries(journalId)

    /**
     * Retrieves all journals, where each journal includes a list of its entries.
     *
     * @return A [Flow] emitting a list of [JournalWithEntries].
     */
    fun getAllJournalsWithEntries(): Flow<List<JournalWithEntries>> =
        journalWithEntriesDao.getAllJournalsWithAssociatedEntries()

    /**
     * Retrieves a complex composite object containing the Journal, all its Entries,
     * and all its Forecasts.
     *
     * This is the primary data source for dashboards or overview screens that need
     * to calculate statistics based on past entries and predict future arrivals based on forecasts.
     *
     * @param journalId The id of the journal.
     * @return A [Flow] emitting [JournalWithEntriesAndForecasts], or null if not found.
     */
    fun getJournalWithEntriesAndForecasts(journalId: Long): Flow<JournalWithEntriesAndForecasts?> =
        journalWithEntriesAndForecastsDao.getJournalWithEntriesAndForecasts(journalId)
}