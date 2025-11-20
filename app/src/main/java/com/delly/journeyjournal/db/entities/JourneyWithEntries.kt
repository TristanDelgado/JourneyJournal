package com.delly.journeyjournal.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class JourneyWithEntries(
    @Embedded val journey: JourneyEntity,

    @Relation(
        parentColumn = "journeyName", // The Primary Key of the Parent (JourneyEntity)
        entityColumn = "ownerId"      // The Foreign Key in the Child (JourneyEntryEntity)
    )
    val entries: List<JourneyEntryEntity>
)
