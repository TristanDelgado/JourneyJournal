package com.delly.journeyjournal.db.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a one-to-many relationship between a Journey and its Entries.
 *
 * This class allows Room to query the parent [JourneyEntity] and all its associated
 * [JourneyEntryEntity] children in a single query (using @Transaction).
 *
 * @property journey The parent [JourneyEntity].
 * @property entries The list of associated [JourneyEntryEntity] objects.
 */
data class JourneyWithEntries(
    @Embedded val journey: JourneyEntity,

    @Relation(
        parentColumn = "journeyName", // The Primary Key of the Parent (JourneyEntity)
        entityColumn = "ownerId"      // The Foreign Key in the Child (JourneyEntryEntity)
    )
    val entries: List<JourneyEntryEntity>
)
