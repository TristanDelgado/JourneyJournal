package com.delly.journeyjournal.db

import com.delly.journeyjournal.db.dataAccessObjects.JourneyEntityDao
import com.delly.journeyjournal.db.entities.JourneyEntity
import kotlinx.coroutines.flow.Flow

//TODO: Open up and clean these functions

class JournalRepository(private val journeyEntityDao: JourneyEntityDao) {

    fun getAllJourneys(): Flow<List<JourneyEntity>> = journeyEntityDao.getAllJournals()

//    suspend fun getUserById(name: String): JourneyEntity? = journeyEntityDao.getJournalByName(name)
//
//    fun searchUsers(searchQuery: String): Flow<List<JourneyEntity>> =
//        journeyEntityDao.searchJournals("%$searchQuery%")
//
//    suspend fun insertUser(journeyEntity: JourneyEntity): Long =
//        journeyEntityDao.insertJournal(journeyEntity)
//
//    suspend fun insertUsers(journeyEntity: List<JourneyEntity>) =
//        journeyEntityDao.insertJournals(journeyEntity)
//
//    suspend fun updateUser(journeyEntity: JourneyEntity) =
//        journeyEntityDao.updateJournal(journeyEntity)
//
//    suspend fun deleteUser(journeyEntity: JourneyEntity) =
//        journeyEntityDao.deleteJournal(journeyEntity)
//
//    suspend fun deleteUserById(name: String) = journeyEntityDao.deleteJournalByName(name)
//
//    suspend fun deleteAllUsers() = journeyEntityDao.deleteAllJournals()
}