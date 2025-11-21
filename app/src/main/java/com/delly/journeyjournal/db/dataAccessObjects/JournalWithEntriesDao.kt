package com.delly.journeyjournal.db.dataAccessObjects

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.delly.journeyjournal.db.entities.JournalWithEntries

/**
 * Data Access Object (DAO) for composite operations involving Journeys.
 *
 * Used when you need to retrieve a journey along with its associated entries.
 */
@Dao
interface JournalWithEntriesDao {
    /**
     * Retrieves a Journey and all its associated entries.
     *
     * The @Transaction annotation is required because this method runs two queries atomically:
     * 1. Selects the Journey.
     * 2. Selects the Entries where the foreign key matches.
     *
     * @param journeyName The unique name of the journey to retrieve.
     * @return A [JournalWithEntries] object containing the journey and its entries.
     */
    @Transaction
    @Query(value = "SELECT * FROM JournalEntity WHERE journalName = :journeyName")
    suspend fun getJourneyWithEntries(journeyName: String): JournalWithEntries
}
