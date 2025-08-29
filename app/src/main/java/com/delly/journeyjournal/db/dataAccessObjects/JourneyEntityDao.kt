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
 * Data Access Object (DAO) for performing CRUD operations on the journeyEntities table.
 *
 * This interface defines all database operations for JourneyEntity objects, including
 * querying, inserting, updating, and deleting journey records.
 */
@Dao
interface JourneyEntityDao {

    /**
     * Retrieves all journal entries from the database, ordered by journal name in ascending order.
     *
     * This method returns a Flow that emits a new list whenever the underlying data changes,
     * making it ideal for observing data changes in the UI layer.
     *
     * @return A Flow that emits a list of all JourneyEntity objects, sorted alphabetically by journalName
     */
    @Query("SELECT * FROM JourneyEntity ORDER BY journalName ASC")
    fun getAllJournals(): Flow<List<JourneyEntity>>
//
//    /**
//     * Retrieves a specific journal entry by its name.
//     *
//     * @param journalName The name of the journal to retrieve
//     * @return The JourneyEntity with the specified name, or null if not found
//     */
//    @Query("SELECT * FROM journey_entities WHERE journalName = :journalName")
//    suspend fun getJournalByName(journalName: String): JourneyEntity?
//
//    /**
//     * Searches for journal entries whose names contain the specified query string.
//     *
//     * This method performs a case-sensitive LIKE search on the journalName field.
//     * The search query should include wildcards (%) if needed for partial matching.
//     *
//     * @param searchQuery The search string to match against journal names (supports SQL LIKE patterns)
//     * @return A Flow that emits a list of JourneyEntity objects matching the search criteria
//     */
//    @Query("SELECT * FROM journey_entities WHERE journalName LIKE :searchQuery")
//    fun searchJournals(searchQuery: String): Flow<List<JourneyEntity>>
//
//    /**
//     * Inserts a new journal entry into the database.
//     *
//     * If a journal with the same primary key already exists, it will be replaced
//     * due to the REPLACE conflict strategy.
//     *
//     * @param journeyEntity The JourneyEntity to insert
//     * @return The row ID of the newly inserted journal entry
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertJournal(journeyEntity: JourneyEntity): Long
//
//    /**
//     * Inserts multiple journal entries into the database in a single transaction.
//     *
//     * If any journals with the same primary keys already exist, they will be replaced
//     * due to the REPLACE conflict strategy.
//     *
//     * @param journeyEntities The list of JourneyEntity objects to insert
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertJournals(journeyEntities: List<JourneyEntity>)
//
//    /**
//     * Updates an existing journal entry in the database.
//     *
//     * The journal is identified by its primary key, and all fields will be updated
//     * with the values from the provided JourneyEntity object.
//     *
//     * @param journeyEntity The JourneyEntity with updated values
//     */
//    @Update
//    suspend fun updateJournal(journeyEntity: JourneyEntity)
//
//    /**
//     * Deletes a journal entry from the database.
//     *
//     * The journal is identified by its primary key.
//     *
//     * @param journeyEntity The JourneyEntity to delete
//     */
//    @Delete
//    suspend fun deleteJournal(journeyEntity: JourneyEntity)
//
//    /**
//     * Deletes a journal entry by its name.
//     *
//     * @param journalName The name of the journal to delete
//     */
//    @Query("DELETE FROM journey_entities WHERE journalName = :journalName")
//    suspend fun deleteJournalByName(journalName: String)
//
//    /**
//     * Deletes all journal entries from the database.
//     */
//    @Query("DELETE FROM journey_entities")
//    suspend fun deleteAllJournals()
}