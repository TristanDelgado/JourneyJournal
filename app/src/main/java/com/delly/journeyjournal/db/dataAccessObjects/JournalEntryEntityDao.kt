package com.delly.journeyjournal.db.dataAccessObjects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.delly.journeyjournal.db.entities.JournalEntryEntity

/**
 * Data Access Object (DAO) for the journeyEntryEntities table.
 */
@Dao
interface JournalEntryEntityDao {
    /**
     * Retrieves a specific entry by its unique ID.
     *
     * @param id The unique identifier of the entry.
     * @return The JourneyEntryEntity, or null if not found.
     */
    @Query("SELECT * FROM JournalEntryEntity WHERE id = :id")
    suspend fun getEntryById(id: Int): JournalEntryEntity?

    // Returns the most recent entry for a specific journey ID
    @Query("SELECT * FROM JournalEntryEntity WHERE ownerId = :journalId ORDER BY id DESC LIMIT 1")
    fun getLastEntryForJournal(journalId: Int): JournalEntryEntity?

    /**
     * Inserts a new journey entry into the database.
     *
     * If an entry with the same primary key already exists, it will be replaced.
     *
     * @param entry The JourneyEntryEntity to insert.
     * @return The row ID of the newly inserted entry.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntryEntity): Long

    /**
     * Updates an existing journey entry in the database.
     *
     * @param entry The JourneyEntryEntity with updated values.
     */
    @Update
    suspend fun updateEntry(entry: JournalEntryEntity)

    /**
     * Deletes a specific journey entry.
     *
     * @param entry The JourneyEntryEntity to delete.
     */
    @Delete
    suspend fun deleteEntry(entry: JournalEntryEntity)
}