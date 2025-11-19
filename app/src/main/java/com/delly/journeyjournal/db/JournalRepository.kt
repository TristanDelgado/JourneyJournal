package com.delly.journeyjournal.db

import com.delly.journeyjournal.db.dataAccessObjects.JourneyEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JourneyEntryEntityDao
import com.delly.journeyjournal.db.entities.JourneyEntity
import com.delly.journeyjournal.db.entities.JourneyEntryEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journeyEntityDao: JourneyEntityDao, private val journeyEntryEntityDao: JourneyEntryEntityDao) {

    // Journal Specific Operations
    fun getAllJourneys(): Flow<List<JourneyEntity>> = journeyEntityDao.getAllJournals()

    suspend fun getJourneyByName(name: String): JourneyEntity? =
        journeyEntityDao.getJournalByName(name)

    suspend fun insertJourney(journeyEntity: JourneyEntity): Long =
        journeyEntityDao.insertJourney(journeyEntity)

    suspend fun insertEntry(journeyName: String, journalEntry: JourneyEntryEntity): Boolean =
        journeyEntityDao.insertEntry(journeyName, journalEntry)

    suspend fun insertJourneys(journeyEntity: List<JourneyEntity>) =
        journeyEntityDao.insertJournals(journeyEntity)

    suspend fun updateJourney(journeyEntity: JourneyEntity) =
        journeyEntityDao.updateJournal(journeyEntity)

    suspend fun deleteUser(journeyEntity: JourneyEntity) =
        journeyEntityDao.deleteJournal(journeyEntity)

    suspend fun deleteJourneyByName(name: String) = journeyEntityDao.deleteJournalByName(name)

    suspend fun deleteAllJourneys() = journeyEntityDao.deleteAllJournals()

    // Entry Specific Operations

    suspend fun getEntryById(id: Int): JourneyEntryEntity? =
        journeyEntryEntityDao.getEntryById(id)

    suspend fun getEntriesByIds(ids: List<Long>): List<JourneyEntryEntity> =
        journeyEntryEntityDao.getEntriesByIds(ids)

    suspend fun insertEntry(entry: JourneyEntryEntity): Long =
        journeyEntryEntityDao.insertEntry(entry)

    suspend fun updateEntry(entry: JourneyEntryEntity) =
        journeyEntryEntityDao.updateEntry(entry)

    suspend fun deleteEntry(entry: JourneyEntryEntity) =
        journeyEntryEntityDao.deleteEntry(entry)
}