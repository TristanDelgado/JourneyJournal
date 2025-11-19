package com.delly.journeyjournal.db.dataAccessObjects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.delly.journeyjournal.db.entities.JourneyEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for performing CRUD operations on the journeyEntryEntities table.
 *
 * This interface defines all database operations for JourneyEntryEntity objects, including
 * querying, inserting, updating, and deleting individual journal entries.
 */
@Dao
interface JourneyEntryEntityDao {
    /**
     * Retrieves a specific entry by its unique ID.
     *
     * @param id The unique identifier of the entry.
     * @return The JourneyEntryEntity, or null if not found.
     */
    @Query("SELECT * FROM JourneyEntryEntity WHERE id = :id")
    suspend fun getEntryById(id: Int): JourneyEntryEntity?

    /**
     * Retrieves a list of entries that match a provided list of IDs.
     *
     * @param ids A list of unique identifiers to fetch.
     * @return A list of matching JourneyEntryEntity objects.
     */
    @Query("SELECT * FROM JourneyEntryEntity WHERE id IN (:ids)")
    suspend fun getEntriesByIds(ids: List<Long>): List<JourneyEntryEntity>

    /**
     * Inserts a new journey entry into the database.
     *
     * If an entry with the same primary key already exists, it will be replaced.
     *
     * @param entry The JourneyEntryEntity to insert.
     * @return The row ID of the newly inserted entry.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JourneyEntryEntity): Long

    /**
     * Updates an existing journey entry in the database.
     *
     * @param entry The JourneyEntryEntity with updated values.
     */
    @Update
    suspend fun updateEntry(entry: JourneyEntryEntity)

    /**
     * Deletes a specific journey entry.
     *
     * @param entry The JourneyEntryEntity to delete.
     */
    @Delete
    suspend fun deleteEntry(entry: JourneyEntryEntity)
}