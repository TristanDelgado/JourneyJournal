package com.delly.journeyjournal.db.dataAccessObjects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.delly.journeyjournal.db.entities.JournalEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the journalEntities table.
 */
@Dao
interface JournalEntityDao {

    /**
     * Retrieves all journal entries from the database, ordered by journal name in ascending order.
     *
     * @return A Flow of all JourneyEntity objects, sorted alphabetically by journalName
     */
    @Query("SELECT * FROM JournalEntity ORDER BY journalName ASC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    /**
     * Retrieves a specific journal entry by its id.
     *
     * @param journalId The id of the journal to retrieve
     * @return The JourneyEntity with the specified id, or null if not found
     */
    @Query("SELECT * FROM JournalEntity WHERE id = :journalId")
    suspend fun getJournalById(journalId: Long): JournalEntity?

    /**
     * Inserts a new journal into the database.
     *
     * If a journal with the same primary key already exists, it will be replaced
     * due to the REPLACE conflict strategy.
     *
     * @param journeyEntity The JourneyEntity to insert
     * @return The row ID of the newly inserted journal entry
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journeyEntity: JournalEntity): Long

    /**
     * Updates an existing journal entry in the database.
     *
     * The journal is identified by its primary key, and all fields will be updated
     * with the values from the provided JourneyEntity object.
     *
     * @param journeyEntity The JourneyEntity with updated values
     */
    @Update
    suspend fun updateJournal(journeyEntity: JournalEntity)

    /**
     * Deletes a journal entry from the database.
     *
     * The journal is identified by its primary key.
     *
     * @param journeyEntity The JourneyEntity to delete
     */
    @Delete
    fun deleteJournal(journeyEntity: JournalEntity)

    /**
     * Deletes all journey entries from the database.
     */
    @Query("DELETE FROM JournalEntity")
    suspend fun deleteAllJournals()
}
