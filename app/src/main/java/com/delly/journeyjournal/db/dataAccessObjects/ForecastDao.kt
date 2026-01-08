package com.delly.journeyjournal.db.dataAccessObjects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.delly.journeyjournal.db.entities.ForecastEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing and managing [ForecastEntity] data in the database.
 *
 * This interface defines the database interactions required for the Forecast feature,
 * providing methods to insert, query, update, and delete forecast records from the
 * 'forecasts' table.
 */
@Dao
interface ForecastDao {

    /**
     * Inserts a new forecast into the database.
     *
     * If a forecast with the same primary key already exists, the existing record
     * will be replaced due to [OnConflictStrategy.REPLACE].
     *
     * @param forecast The [ForecastEntity] object to be persisted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: ForecastEntity)

    /**
     * Retrieves a reactive stream of all forecasts associated with a specific journal.
     *
     * The returned [Flow] will emit a new list of forecasts immediately upon subscription
     * and subsequently whenever the data in the 'forecasts' table changes for the given [journalId].
     *
     * @param journalId The unique identifier of the journal to retrieve forecasts for.
     * @return A [Flow] emitting a [List] of [ForecastEntity] objects.
     */
    @Query("SELECT * FROM forecasts WHERE journalId = :journalId")
    fun getForecastsForJournal(journalId: Long): Flow<List<ForecastEntity>>

    /**
     * Deletes a specific forecast from the database.
     *
     * @param id The unique identifier (primary key) of the forecast to be deleted.
     */
    @Query("DELETE FROM forecasts WHERE id = :id")
    suspend fun deleteForecast(id: Long)

    /**
     * Updates the editable fields (name and mile marker) of an existing forecast.
     *
     * @param id The unique identifier of the forecast to update.
     * @param name The new name for the forecast.
     * @param mileMarker The new mile marker location for the forecast.
     */
    @Query("UPDATE forecasts SET name = :name, mileMarker = :mileMarker WHERE id = :id")
    suspend fun updateForecast(
        id: Long,
        name: String,
        mileMarker: Double,
    )
}