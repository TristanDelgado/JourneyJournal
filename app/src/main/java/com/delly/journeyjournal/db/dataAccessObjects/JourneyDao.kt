package com.delly.journeyjournal.db.dataAccessObjects

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.delly.journeyjournal.db.entities.JourneyWithEntries

@Dao
interface JourneyDao {
    // The @Transaction annotation is required because Room runs two queries behind the scenes
    // 1. Select Journey
    // 2. Select Entries where journeyName = journey.name
    @Transaction
    @Query(value = "SELECT * FROM JourneyEntity WHERE journeyName = :journeyName")
    suspend fun getJourneyWithEntries(journeyName: String): JourneyWithEntries
}