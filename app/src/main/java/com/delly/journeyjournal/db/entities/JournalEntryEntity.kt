package com.delly.journeyjournal.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a single entry within a journey. This is a supporting data class that is intended to be
 * referenced by a main journey entity.
 *
 * @property dayNumber The day number of the entry.
 * @property startLocation The starting location of the journey entry.
 * @property endLocation The ending location of the journey entry.
 * @property distanceHiked The distance hiked during the journey entry.
 * @property trailConditions The conditions of the trail during the journey entry.
 * @property wildlifeSightings Any wildlife sighted during the journey entry.
 * @property resupplyNotes Notes on resupply or water during the journey entry.
 * @property notes General notes and reflections for the journey entry.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = JournalEntity::class,
            parentColumns = ["journalName"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerId: String,
    val dayNumber: String,
    val startLocation: String,
    val endLocation: String,
    val distanceHiked: String,
    val trailConditions: String,
    val wildlifeSightings: String,
    val resupplyNotes: String,
    val notes: String
)
