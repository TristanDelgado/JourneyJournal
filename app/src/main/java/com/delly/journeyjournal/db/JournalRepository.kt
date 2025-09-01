package com.delly.journeyjournal.db

import com.delly.journeyjournal.db.dataAccessObjects.JourneyEntityDao
import com.delly.journeyjournal.db.entities.JourneyEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journeyEntityDao: JourneyEntityDao) {

    fun getAllJourneys(): Flow<List<JourneyEntity>> = journeyEntityDao.getAllJournals()

    suspend fun getJourneyByName(name: String): JourneyEntity? = journeyEntityDao.getJournalByName(name)

    suspend fun insertJourney(journeyEntity: JourneyEntity): Long =
        journeyEntityDao.insertJourney(journeyEntity)

    suspend fun insertJourneys(journeyEntity: List<JourneyEntity>) =
        journeyEntityDao.insertJournals(journeyEntity)

    suspend fun updateJourney(journeyEntity: JourneyEntity) =
        journeyEntityDao.updateJournal(journeyEntity)

    suspend fun deleteUser(journeyEntity: JourneyEntity) =
        journeyEntityDao.deleteJournal(journeyEntity)

    suspend fun deleteJourneyByName(name: String) = journeyEntityDao.deleteJournalByName(name)

    suspend fun deleteAllJourneys() = journeyEntityDao.deleteAllJournals()
}