package com.delly.journeyjournal.db.dataAccessObjects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.delly.journeyjournal.db.entities.JourneyEntity
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
    @Query("SELECT * FROM JourneyEntity ORDER BY journeyName ASC")
    fun getAllJournals(): Flow<List<JourneyEntity>>

    /**
     * Retrieves a specific journal entry by its name.
     *
     * @param journeyName The name of the journal to retrieve
     * @return The JourneyEntity with the specified name, or null if not found
     */
    @Query("SELECT * FROM JourneyEntity WHERE journeyName = :journeyName")
    suspend fun getJournalByName(journeyName: String): JourneyEntity?

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
    suspend fun insertJournal(journeyEntity: JourneyEntity): Long

    /**
     * Updates an existing journal entry in the database.
     *
     * The journal is identified by its primary key, and all fields will be updated
     * with the values from the provided JourneyEntity object.
     *
     * @param journeyEntity The JourneyEntity with updated values
     */
    @Update
    suspend fun updateJournal(journeyEntity: JourneyEntity)

    /**
     * Deletes a journal entry from the database.
     *
     * The journal is identified by its primary key.
     *
     * @param journeyEntity The JourneyEntity to delete
     */
    @Delete
    suspend fun deleteJournal(journeyEntity: JourneyEntity)

    /**
     * Deletes all journey entries from the database.
     */
    @Query("DELETE FROM JourneyEntity")
    suspend fun deleteAllJournals()
}