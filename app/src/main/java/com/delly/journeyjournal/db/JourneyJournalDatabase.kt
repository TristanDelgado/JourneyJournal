package com.delly.journeyjournal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.delly.journeyjournal.db.dataAccessObjects.ForecastDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntryEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesAndForecastsDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesDao
import com.delly.journeyjournal.db.entities.ForecastEntity
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.enums.DistanceUnit
import com.delly.journeyjournal.enums.TransportationMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * The Room Database that contains the Journal, Entry, and Forecast tables.
 *
 * This database serves as the main access point to the persisted data of the application.
 * It defines the entities, version, and type converters used.
 *
 * @property journalEntityDao Returns the [JournalEntityDao].
 * @property journalEntryEntityDao Returns the [JournalEntryEntityDao].
 * @property journalWithEntriesDao Returns the [JournalWithEntriesDao] for composite queries.
 */
@Database(
    entities = [JournalEntity::class, JournalEntryEntity::class, ForecastEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JourneyJournalDatabase : RoomDatabase() {

    abstract fun journalEntityDao(): JournalEntityDao
    abstract fun journalEntryEntityDao(): JournalEntryEntityDao
    abstract fun journalWithEntriesDao(): JournalWithEntriesDao
    abstract fun forecastDao(): ForecastDao
    abstract fun journalWithEntriesAndForecastsDao(): JournalWithEntriesAndForecastsDao

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
        fun getDatabase(
            context: Context,
            scope: CoroutineScope,
        ): JourneyJournalDatabase {
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
            private val scope: CoroutineScope,
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(
                            database.journalEntityDao(),
                            database.journalEntryEntityDao(),
                            database.forecastDao()
                        )
                    }
                }
            }

            suspend fun populateDatabase(
                journalDao: JournalEntityDao,
                entryDao: JournalEntryEntityDao,
                forecastDao: ForecastDao,
            ) {
                // 1. Create a Base Date (e.g., 5 days ago) so entries are sequential
                val calendar = Calendar.getInstance()
                calendar.add(
                    Calendar.DAY_OF_YEAR,
                    -5
                )
                val startDateValue = calendar.timeInMillis

                // 2. Add Default Journal
                val journal = JournalEntity(
                    journalName = "Appalachian Trail 2026",
                    journeymanName = "Hiker John",
                    courseName = "Appalachian Trail",
                    courseRegion = "Georgia -> Maine",
                    startDate = startDateValue,
                    transportationMethod = TransportationMethods.ON_FOOT,
                    description = "Attempting a thru-hike starting from Springer Mountain.",
                    distanceUnit = DistanceUnit.MILES,
                    isComplete = false
                )
                val journalId = journalDao.insertJournal(journal)

                // 3. Add Realistic Entries (First ~53 miles of the AT)
                val entries = listOf(
                    JournalEntryEntity(
                        ownerId = journalId,
                        date = getDatePlusDays(
                            startDateValue,
                            0
                        ),
                        dayNumber = "1",
                        startLocation = "Springer Mountain",
                        endLocation = "Hawk Mountain Shelter",
                        startMileMarker = "0.0",
                        endMileMarker = "8.1",
                        distanceHiked = "8.1",
                        elevationStart = "3780",
                        elevationEnd = "3200",
                        netElevationChange = "-580", // Net drop
                        trailConditions = "Clear and sunny, lots of roots.",
                        wildlifeSightings = "Saw a deer near the parking lot.",
                        resupplyNotes = "Full pack, no need.",
                        notes = "First day on trail! The stairs at Amicalola were brutal, but Springer feels magical.",
                        sleptInBed = false,
                        tookShower = false,
                        weather = "Sunny",
                        dayRating = "5",
                        moodRating = "Excited"
                    ),
                    JournalEntryEntity(
                        ownerId = journalId,
                        date = getDatePlusDays(
                            startDateValue,
                            1
                        ),
                        dayNumber = "2",
                        startLocation = "Hawk Mountain Shelter",
                        endLocation = "Gooch Mountain Shelter",
                        startMileMarker = "8.1",
                        endMileMarker = "15.8",
                        distanceHiked = "7.7",
                        elevationStart = "3200",
                        elevationEnd = "2780",
                        netElevationChange = "-420",
                        trailConditions = "Muddy in the gaps.",
                        wildlifeSightings = "",
                        resupplyNotes = "",
                        notes = "Shorter day today to get my trail legs under me. Met a cool group at the shelter.",
                        sleptInBed = false,
                        tookShower = false,
                        weather = "Cloudy",
                        dayRating = "4",
                        moodRating = "Tired"
                    ),
                    JournalEntryEntity(
                        ownerId = journalId,
                        date = getDatePlusDays(
                            startDateValue,
                            2
                        ),
                        dayNumber = "3",
                        startLocation = "Gooch Mountain Shelter",
                        endLocation = "Neel Gap (Mountain Crossings)",
                        startMileMarker = "15.8",
                        endMileMarker = "31.7",
                        distanceHiked = "15.9",
                        elevationStart = "2780",
                        elevationEnd = "3125",
                        netElevationChange = "+345",
                        trailConditions = "Rocky ascent up Blood Mountain.",
                        wildlifeSightings = "Red-tailed hawk.",
                        resupplyNotes = "Bought fuel and a pizza.",
                        notes = "Big day! Conquered Blood Mountain. Staying at the hostel tonight for a shower.",
                        sleptInBed = true, // Hostel
                        tookShower = true,
                        weather = "Windy",
                        dayRating = "5",
                        moodRating = "Accomplished"
                    ),
                    JournalEntryEntity(
                        ownerId = journalId,
                        date = getDatePlusDays(
                            startDateValue,
                            3
                        ),
                        dayNumber = "4",
                        startLocation = "Neel Gap",
                        endLocation = "Low Gap Shelter",
                        startMileMarker = "31.7",
                        endMileMarker = "42.9",
                        distanceHiked = "11.2",
                        elevationStart = "3125",
                        elevationEnd = "2950",
                        netElevationChange = "-175",
                        trailConditions = "Rain all afternoon.",
                        wildlifeSightings = "",
                        resupplyNotes = "",
                        notes = "Leaving the comfort of the hostel was hard. Rain made the descent slippery.",
                        sleptInBed = false,
                        tookShower = false,
                        weather = "Rainy",
                        dayRating = "2",
                        moodRating = "Miserable"
                    ),
                    JournalEntryEntity(
                        ownerId = journalId,
                        date = getDatePlusDays(
                            startDateValue,
                            4
                        ),
                        dayNumber = "5",
                        startLocation = "Low Gap Shelter",
                        endLocation = "Blue Mountain Shelter",
                        startMileMarker = "42.9",
                        endMileMarker = "52.9",
                        distanceHiked = "10.0",
                        elevationStart = "2950",
                        elevationEnd = "3800",
                        netElevationChange = "+850",
                        trailConditions = "Drying out, beautiful views.",
                        wildlifeSightings = "Wild Turkeys.",
                        resupplyNotes = "Running low on snacks.",
                        notes = "Back in the groove. Blue Mountain has an amazing sunset view.",
                        sleptInBed = false,
                        tookShower = false,
                        weather = "Sunny",
                        dayRating = "4",
                        moodRating = "Content"
                    ),
                    JournalEntryEntity(
                        ownerId = journalId,
                        date = getDatePlusDays(
                            startDateValue,
                            5
                        ),
                        dayNumber = "6",
                        startLocation = "Blue Mountain Shelter",
                        endLocation = "Blue Mountain Shelter",
                        startMileMarker = "52.9",
                        endMileMarker = "52.9",
                        distanceHiked = "0.0",
                        elevationStart = "3800",
                        elevationEnd = "3800",
                        netElevationChange = "0",
                        trailConditions = "N/A (Zero Day)",
                        wildlifeSightings = "Chipmunks near the shelter.",
                        resupplyNotes = "Ate extra rations to lighten the pack.",
                        notes = "Decided to take a zero day to let my knees recover. Spent the day reading and enjoying the view from Blue Mountain.",
                        sleptInBed = false,
                        tookShower = false,
                        weather = "Sunny",
                        dayRating = "5",
                        moodRating = "Restful"
                    )
                )

                entries.forEach { entryDao.insertJournalEntry(it) }

                // 4. Add Forecasts (Real landmarks ahead of Mile 52.9)
                // Avg pace so far is ~10.5 miles/day, so calculations should look good.

                val forecast1 = ForecastEntity(
                    journalId = journalId,
                    name = "Top of Georgia Hostel",
                    mileMarker = 69.6 // ~16.7 miles away (~1.5 days)
                )
                val forecast2 = ForecastEntity(
                    journalId = journalId,
                    name = "Franklin, NC (Winding Stair Gap)",
                    mileMarker = 109.8 // ~56.9 miles away (~5.5 days)
                )
                val forecast3 = ForecastEntity(
                    journalId = journalId,
                    name = "Nantahala Outdoor Center (NOC)",
                    mileMarker = 137.1 // ~84.2 miles away (~8 days)
                )

                forecastDao.insertForecast(forecast1)
                forecastDao.insertForecast(forecast2)
                forecastDao.insertForecast(forecast3)
            }

            // Helper to prevent using the exact same millisecond for every entry
            private fun getDatePlusDays(
                baseTime: Long,
                daysToAdd: Int,
            ): Long {
                val cal = Calendar.getInstance()
                cal.timeInMillis = baseTime
                cal.add(
                    Calendar.DAY_OF_YEAR,
                    daysToAdd
                )
                return cal.timeInMillis
            }
        }
    }
}