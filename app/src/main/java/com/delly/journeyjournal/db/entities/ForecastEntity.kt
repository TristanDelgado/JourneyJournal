package com.delly.journeyjournal.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a future waypoint or milestone within a specific hiking journal.
 *
 * This entity creates the 'forecasts' table in the database. A forecast is used to
 * track a specific location ahead on the trail (e.g., a town, a summit, or a shelter)
 * defined by its [name] and [mileMarker]. The application uses these records to
 * calculate estimated arrival dates based on the user's current hiking pace.
 *
 * **Foreign Key Relationship:**
 * This entity has a Many-to-One relationship with [JournalEntity].
 * If the parent Journal is deleted, all associated Forecasts will be automatically
 * deleted ([ForeignKey.CASCADE]).
 *
 * @property id The unique, auto-generated primary key for this forecast.
 * @property journalId The foreign key referencing the owner [JournalEntity].
 * @property name The display name of the location or landmark (e.g., "Harper's Ferry").
 * @property mileMarker The specific distance marker on the trail where this landmark is located.
 */
@Entity(
    tableName = "forecasts",
    foreignKeys = [
        ForeignKey(
            entity = JournalEntity::class,
            parentColumns = ["id"],
            childColumns = ["journalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["journalId"])]
)
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val journalId: Long,
    val name: String,
    val mileMarker: Double,
)