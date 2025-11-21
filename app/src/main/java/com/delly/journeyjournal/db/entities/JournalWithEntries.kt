package com.delly.journeyjournal.db.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a one-to-many relationship between a Journey and its Entries.
 *
 * This class allows Room to query the parent [JournalEntity] and all its associated
 * [JournalEntryEntity] children in a single query (using @Transaction).
 *
 * @property journey The parent [JournalEntity].
 * @property entries The list of associated [JournalEntryEntity] objects.
 */
data class JournalWithEntries(
    @Embedded val journey: JournalEntity,

    @Relation(
        parentColumn = "journalName", // The Primary Key of the Parent (JournalEntity)
        entityColumn = "ownerId"      // The Foreign Key in the Child (JourneyEntryEntity)
    )
    val entries: List<JournalEntryEntity>
)
