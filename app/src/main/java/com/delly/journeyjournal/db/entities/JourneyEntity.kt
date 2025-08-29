package com.delly.journeyjournal.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.delly.journeyjournal.enums.TransportationMethods

/**
 * Represents a journey entity in the database.
 *
 * @property journalName The name of the journey.
 * @property journeymanName The name of the journeyman.
 * @property courseName The name of the course.
 * @property courseRegion The region of the course.
 * @property startDate The start date of the journey.
 * @property transportationMethod The transportation method used on the journey.
 * @property description The description of the journey.
 */
@Entity(tableName = "JourneyEntity")
data class JourneyEntity(
    @PrimaryKey val journalName: String,
    val journeymanName: String,
    val courseName: String,
    val courseRegion: String,
    val startDate: Long,
    val transportationMethod: TransportationMethods,
    val description: String
)