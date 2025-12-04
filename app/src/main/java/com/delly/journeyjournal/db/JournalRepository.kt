package com.delly.journeyjournal.db

import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntryEntityDao
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.db.entities.JournalWithEntries
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to multiple data sources.
 * It acts as a mediator between the domain/UI layer and the data layer (DAOs).
 *
 * @property journeyEntityDao DAO for [JournalEntity] operations.
 * @property journeyEntryEntityDao DAO for [JournalEntryEntity] operations.
 * @property journeyDao DAO for composite operations like [JournalWithEntries].
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
     * @return A [Flow] emitting a list of [JournalEntity] objects.
     */
    fun getAllJournals(): Flow<List<JournalEntity>> =
        journeyEntityDao.getAllJournals()

    /**
     * Retrieves a specific journal by its id.
     *
     * @param id The id of the journal.
     * @return The [JournalEntity] if found, null otherwise.
     */
    suspend fun getJournalById(id: Int): JournalEntity? =
        journeyEntityDao.getJournalById(journalId = id)

    /**
     * Inserts a new journal into the database.
     *
     * @param journalEntity The [journalEntity] to insert.
     * @return The row ID of the inserted journey.
     */
    suspend fun insertJournal(journalEntity: JournalEntity): Long =
        journeyEntityDao.insertJournal(journalEntity)

    /**
     * Updates an existing journal.
     *
     * @param journalEntity The [journalEntity] with updated values.
     */
    suspend fun updateJournal(journalEntity: JournalEntity) =
        journeyEntityDao.updateJournal(journalEntity)

    /**
     * Deletes a specific journey.
     *
     * @param journalEntity The [journalEntity] to delete.
     */
    suspend fun deleteJournal(journalEntity: JournalEntity) =
        journeyEntityDao.deleteJournal(journalEntity)

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
     * @return The [JournalEntryEntity] if found, null otherwise
     */
    suspend fun getEntryById(id: Int): JournalEntryEntity? =
        journeyEntryEntityDao.getEntryById(id = id)

    /**
     * Inserts a new journey entry.
     *
     * @param entry The [JournalEntryEntity] to insert.
     * @return The row ID of the inserted entry.
     */
    suspend fun insertJourneyEntry(entry: JournalEntryEntity): Long =
        journeyEntryEntityDao.insertJourneyEntry(entry)

    /**
     * Update an existing journey entry
     *
     * @param entry The [JournalEntryEntity] with updated values
     */
    suspend fun updateEntry(entry: JournalEntryEntity) =
        journeyEntryEntityDao.updateEntry(entry)

    /**
     * Delete entry
     *
     * @param entry The [JournalEntryEntity] to delete found via its primary key
     */
    suspend fun deleteEntry(entry: JournalEntryEntity) =
        journeyEntryEntityDao.deleteEntry(entry)

    // Getting a coupled Journal and its related entries

    /**
     * Retrieves a journey along with its associated entries.
     *
     * @param journalId The id of the journey.
     * @return A [JournalWithEntries] object containing the journey and its entries.
     */
    suspend fun getJourneyWithEntries(journalId: Int): JournalWithEntries =
        journeyDao.getJourneyWithEntries(journalId)
}
