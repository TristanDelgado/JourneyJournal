package com.delly.journeyjournal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntryEntityDao
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.enums.DistanceUnit
import com.delly.journeyjournal.enums.TransportationMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

/**
 * The Room Database that contains the Journey and JourneyEntry tables.
 *
 * This database serves as the main access point to the persisted data of the application.
 * It defines the entities, version, and type converters used.
 *
 * @property journalEntityDao Returns the [JournalEntityDao].
 * @property journalEntryEntityDao Returns the [JournalEntryEntityDao].
 * @property journalDao Returns the [JournalWithEntriesDao] for composite queries.
 */
@Database(
    entities = [JournalEntity::class, JournalEntryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JourneyJournalDatabase : RoomDatabase() {

    abstract fun journalEntityDao(): JournalEntityDao
    abstract fun journalEntryEntityDao(): JournalEntryEntityDao
    abstract fun journalDao(): JournalWithEntriesDao

    companion object {
        @Volatile
        private var INSTANCE: JourneyJournalDatabase? = null

        /**
         * Gets the singleton instance of the [JourneyJournalDatabase].
         *
         * If the instance is not created yet, it builds the database using Room.
         * This implementation uses double-checked locking to ensure thread safety.
         *
         * @param context The application context.
         * @return The singleton [JourneyJournalDatabase] instance.
         */
        fun getDatabase(context: Context, scope: CoroutineScope): JourneyJournalDatabase {
            return INSTANCE ?: synchronized(lock = this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    klass = JourneyJournalDatabase::class.java,
                    name = "journey_journal_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = false) // Use only for development
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.journalEntityDao(), database.journalEntryEntityDao())
                    }
                }
            }

            suspend fun populateDatabase(journalDao: JournalEntityDao, entryDao: JournalEntryEntityDao) {
                // Add default journal
                val journal = JournalEntity(
                    journalName = "My First Journal",
                    journeymanName = "John Doe",
                    courseName = "Appalachian Trail",
                    courseRegion = "Eastern United States",
                    startDate = Date().time,
                    transportationMethod = TransportationMethods.ON_FOOT,
                    description = "My first thru-hike!",
                    distanceUnit = DistanceUnit.MILES,
                    isComplete = false
                )
                val journalId = journalDao.insertJournal(journal)

                // Add default entries
                for (i in 1..5) {
                    val entry = JournalEntryEntity(
                        ownerId = journalId,
                        date = Date().time,
                        dayNumber = i.toString(),
                        startLocation = "Start location $i",
                        endLocation = "End location $i",
                        distanceHiked = "${i * 10}",
                        trailConditions = "Trail conditions $i",
                        wildlifeSightings = "Wildlife sightings $i",
                        resupplyNotes = "Resupply notes $i",
                        notes = "Notes $i",
                        startMileMarker = "${(i - 1) * 10}",
                        endMileMarker = "${i * 10}",
                        elevationStart = "${i * 100}",
                        elevationEnd = "${i * 100 + 50}",
                        netElevationChange = "50",
                        sleptInBed = i % 2 == 0,
                        tookShower = i % 2 != 0,
                        weather = "Sunny",
                        dayRating = "${i % 5 + 1}",
                        moodRating = "${i % 5 + 1}"
                    )
                    entryDao.insertJournalEntry(entry)
                }
            }
        }
    }
}