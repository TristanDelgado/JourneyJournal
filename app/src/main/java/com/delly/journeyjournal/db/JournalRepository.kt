package com.delly.journeyjournal.db

import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntryEntityDao
import com.delly.journeyjournal.db.entities.JourneyEntity
import com.delly.journeyjournal.db.entities.JourneyEntryEntity
import com.delly.journeyjournal.db.entities.JourneyWithEntries
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to multiple data sources.
 * It acts as a mediator between the domain/UI layer and the data layer (DAOs).
 *
 * @property journeyEntityDao DAO for [JourneyEntity] operations.
 * @property journeyEntryEntityDao DAO for [JourneyEntryEntity] operations.
 * @property journeyDao DAO for composite operations like [JourneyWithEntries].
 */
class JournalRepository(
    private val journeyEntityDao: JournalEntityDao,
    private val journeyEntryEntityDao: JournalEntryEntityDao,
    private val journeyDao: JournalWithEntriesDao
) {
    // Journal Specific Operations

    /**
     * Retrieves all journeys as a Flow.
     *
     * @return A [Flow] emitting a list of [JourneyEntity] objects.
     */
    fun getAllJournals(): Flow<List<JourneyEntity>> =
        journeyEntityDao.getAllJournals()

    /**
     * Retrieves a specific journey by its name.
     *
     * @param name The name of the journey.
     * @return The [JourneyEntity] if found, null otherwise.
     */
    suspend fun getJournalByName(name: String): JourneyEntity? =
        journeyEntityDao.getJournalByName(journeyName = name)

    /**
     * Inserts a new journal into the database.
     *
     * @param journeyEntity The [JourneyEntity] to insert.
     * @return The row ID of the inserted journey.
     */
    suspend fun insertJournal(journeyEntity: JourneyEntity): Long =
        journeyEntityDao.insertJournal(journeyEntity)

    /**
     * Updates an existing journal.
     *
     * @param journeyEntity The [JourneyEntity] with updated values.
     */
    suspend fun updateJournal(journeyEntity: JourneyEntity) =
        journeyEntityDao.updateJournal(journeyEntity)

    /**
     * Deletes a specific journey.
     *
     * @param journeyEntity The [JourneyEntity] to delete.
     */
    suspend fun deleteJournal(journeyEntity: JourneyEntity) =
        journeyEntityDao.deleteJournal(journeyEntity)

    /**
     * Deletes all journeys from the database.
     */
    suspend fun deleteAllJournals() =
        journeyEntityDao.deleteAllJournals()

    // Entry Entity Specific Operations

    /**
     * Get entry by id
     *
     * @param id The id of the entry to return
     * @return The [JourneyEntryEntity] if found, null otherwise
     */
    suspend fun getEntryById(id: Int): JourneyEntryEntity? =
        journeyEntryEntityDao.getEntryById(id = id)

    /**
     * Inserts a new journey entry.
     *
     * @param entry The [JourneyEntryEntity] to insert.
     * @return The row ID of the inserted entry.
     */
    suspend fun insertJourneyEntry(entry: JourneyEntryEntity): Long =
        journeyEntryEntityDao.insertJourneyEntry(entry)

    /**
     * Update an existing journey entry
     *
     * @param entry The [JourneyEntryEntity] with updated values
     */
    suspend fun updateEntry(entry: JourneyEntryEntity) =
        journeyEntryEntityDao.updateEntry(entry)

    /**
     * Delete entry
     *
     * @param entry The [JourneyEntryEntity] to delete found via its primary key
     */
    suspend fun deleteEntry(entry: JourneyEntryEntity) =
        journeyEntryEntityDao.deleteEntry(entry)

    // Getting a coupled Journal and its related entries

    /**
     * Retrieves a journey along with its associated entries.
     *
     * @param journeyName The name of the journey.
     * @return A [JourneyWithEntries] object containing the journey and its entries.
     */
    suspend fun getJourneyWithEntries(journeyName: String): JourneyWithEntries =
        journeyDao.getJourneyWithEntries(journeyName)
}
