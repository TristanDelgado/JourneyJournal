package com.delly.journeyjournal.db

import com.delly.journeyjournal.db.dataAccessObjects.JournalEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntryEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesDao
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.db.entities.JournalWithEntries
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to multiple data sources.
 * It acts as a mediator between the domain/UI layer and the data layer (DAOs).
 *
 * @property journalEntityDao DAO for [JournalEntity] operations.
 * @property journalEntryEntityDao DAO for [JournalEntryEntity] operations.
 * @property journalDao DAO for composite operations like [JournalWithEntries].
 */
class JournalRepository(
    private val journalEntityDao: JournalEntityDao,
    private val journalEntryEntityDao: JournalEntryEntityDao,
    private val journalDao: JournalWithEntriesDao,
) {
    // Journal Specific Operations

    /**
     * Retrieves all journals as a Flow.
     *
     * @return A [Flow] emitting a list of [JournalEntity] objects.
     */
    fun getAllJournals(): Flow<List<JournalEntity>> =
        journalEntityDao.getAllJournals()

    /**
     * Retrieves a specific journal by its id.
     *
     * @param id The id of the journal.
     * @return The [JournalEntity] if found, null otherwise.
     */
    suspend fun getJournalById(id: Long): JournalEntity? =
        journalEntityDao.getJournalById(journalId = id)

    /**
     * Inserts a new journal into the database.
     *
     * @param journalEntity The [journalEntity] to insert.
     * @return The row ID of the inserted journal.
     */
    suspend fun insertJournal(journalEntity: JournalEntity): Long =
        journalEntityDao.insertJournal(journalEntity)

    /**
     * Updates an existing journal.
     *
     * @param journalEntity The [journalEntity] with updated values.
     */
    suspend fun updateJournal(journalEntity: JournalEntity) =
        journalEntityDao.updateJournal(journalEntity)

    /**
     * Deletes a specific journal.
     *
     * @param journalEntity The [journalEntity] to delete.
     */
    fun deleteJournal(journalEntity: JournalEntity) =
        journalEntityDao.deleteJournal(journalEntity)

    /**
     * Deletes all journals from the database.
     */
    suspend fun deleteAllJournals() =
        journalEntityDao.deleteAllJournals()

    // Entry Entity Specific Operations

    /**
     * Get entry by id
     *
     * @param id The id of the entry to return
     * @return The [JournalEntryEntity] if found, null otherwise
     */
    suspend fun getEntryById(id: Long): JournalEntryEntity? =
        journalEntryEntityDao.getEntryById(id = id)

    /**
     * Get the last entry in a journal by the journal's main ID
     *
     * @param journalId The id of the journal to get the last entry of
     * @return The [JournalEntryEntity] if found, null otherwise
     */
    fun getLastEntryForJournal(journalId: Long): JournalEntryEntity? =
        journalEntryEntityDao.getLastEntryForJournal(journalId)

    /**
     * Inserts a new journal entry.
     *
     * @param entry The [JournalEntryEntity] to insert.
     * @return The row ID of the inserted entry.
     */
    suspend fun insertJournalEntry(entry: JournalEntryEntity): Long =
        journalEntryEntityDao.insertJournalEntry(entry)

    /**
     * Update an existing journal entry
     *
     * @param entry The [JournalEntryEntity] with updated values
     */
    suspend fun updateEntry(entry: JournalEntryEntity) =
        journalEntryEntityDao.updateEntry(entry)

    /**
     * Delete entry
     *
     * @param entry The [JournalEntryEntity] to delete found via its primary key
     */
    suspend fun deleteEntry(entry: JournalEntryEntity) =
        journalEntryEntityDao.deleteEntry(entry)

    // Getting a coupled Journal and its related entries

    /**
     * Retrieves a journal along with its associated entries.
     *
     * @param journalId The id of the journal.
     * @return A [JournalWithEntries] object containing the journal and its entries.
     */
    fun getJournalWithEntries(journalId: Long): Flow<JournalWithEntries?> =
        journalDao.getJournalWithEntries(journalId)

    /**
     * Retrieves all Journals with their associated entries.
     *
     * @return A [JournalWithEntries] object containing the journal and its entries.
     */
    fun getAllJournalsWithEntries(): Flow<List<JournalWithEntries>> =
        journalDao.getAllJournalsWithAssociatedEntries()
}
