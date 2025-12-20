package com.delly.journeyjournal.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a single entry within a journey. This is a supporting data class that is intended to be
 * referenced by a main journey entity.
 *
 * @property date The date of the entry.
 * @property dayNumber The day number of the entry.
 * @property startLocation The starting location of the journey entry.
 * @property endLocation The ending location of the journey entry.
 * @property distanceHiked The distance hiked during the journey entry.
 * @property trailConditions The conditions of the trail during the journey entry.
 * @property wildlifeSightings Any wildlife sighted during the journey entry.
 * @property resupplyNotes Notes on resupply or water during the journey entry.
 * @property notes General notes and reflections for the journey entry.
 * @property startMileMarker The starting mile marker.
 * @property endMileMarker The ending mile marker.
 * @property elevationStart The starting elevation.
 * @property elevationEnd The ending elevation.
 * @property netElevationChange The net change in elevation.
 * @property sleptInBed Whether the user slept in a bed.
 * @property tookShower Whether the user took a shower.
 * @property weather The weather conditions.
 * @property dayRating The rating for the day.
 * @property moodRating The user's mood rating.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = JournalEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerId: Int,
    val date: Long,
    val dayNumber: String,
    val startLocation: String,
    val endLocation: String,
    val distanceHiked: String,
    val trailConditions: String,
    val wildlifeSightings: String,
    val resupplyNotes: String,
    val notes: String,
    val startMileMarker: String,
    val endMileMarker: String,
    val elevationStart: String,
    val elevationEnd: String,
    val netElevationChange: String,
    val sleptInBed: Boolean,
    val tookShower: Boolean,
    val weather: String,
    val dayRating: String,
    val moodRating: String
)
