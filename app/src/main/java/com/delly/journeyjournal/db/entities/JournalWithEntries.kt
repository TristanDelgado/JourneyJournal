package com.delly.journeyjournal.db.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a one-to-many relationship between a Journal and its Entries.
 *
 * This class allows Room to query the parent [JournalEntity] and all its associated
 * [JournalEntryEntity] children in a single query (using @Transaction).
 *
 * @property journal The parent [JournalEntity].
 * @property entries The list of associated [JournalEntryEntity] objects.
 */
data class JournalWithEntries(
    @Embedded val journal: JournalEntity,

    @Relation(
        parentColumn = "id", // The Primary Key of the Parent (JournalEntity)
        entityColumn = "ownerId" // The Foreign Key in the Child (JournalEntryEntity)
    )
    val entries: List<JournalEntryEntity>
)
