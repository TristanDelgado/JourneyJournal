package com.delly.journeyjournal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.delly.journeyjournal.db.dataAccessObjects.JournalWithEntriesDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JournalEntryEntityDao
import com.delly.journeyjournal.db.entities.JourneyEntity
import com.delly.journeyjournal.db.entities.JourneyEntryEntity

/**
 * The Room Database that contains the Journey and JourneyEntry tables.
 *
 * This database serves as the main access point to the persisted data of the application.
 * It defines the entities, version, and type converters used.
 *
 * @property journeyEntityDao Returns the [JournalEntityDao].
 * @property journeyEntryEntityDao Returns the [JournalEntryEntityDao].
 * @property journeyDao Returns the [JournalWithEntriesDao] for composite queries.
 */
@Database(
    entities = [JourneyEntity::class, JourneyEntryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JourneyJournalDatabase : RoomDatabase() {

    abstract fun journeyEntityDao(): JournalEntityDao
    abstract fun journeyEntryEntityDao(): JournalEntryEntityDao
    abstract fun journeyDao(): JournalWithEntriesDao

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
        fun getDatabase(context: Context): JourneyJournalDatabase {
            return INSTANCE ?: synchronized(lock = this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    klass = JourneyJournalDatabase::class.java,
                    name = "journey_journal_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = false) // Use only for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
