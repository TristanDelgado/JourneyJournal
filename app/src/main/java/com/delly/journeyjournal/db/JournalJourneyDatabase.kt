package com.delly.journeyjournal.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.delly.journeyjournal.db.dataAccessObjects.JourneyDao
import com.delly.journeyjournal.db.dataAccessObjects.JourneyEntityDao
import com.delly.journeyjournal.db.dataAccessObjects.JourneyEntryEntityDao
import com.delly.journeyjournal.db.entities.JourneyEntity
import com.delly.journeyjournal.db.entities.JourneyEntryEntity

@Database(
    entities = [JourneyEntity::class, JourneyEntryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JournalJourneyDatabase : RoomDatabase() {

    abstract fun journeyEntityDao(): JourneyEntityDao
    abstract fun journeyEntryEntityDao(): JourneyEntryEntityDao
    abstract fun journeyDao() : JourneyDao

    companion object {
        @Volatile
        private var INSTANCE: JournalJourneyDatabase? = null

        fun getDatabase(context: Context): JournalJourneyDatabase {
            return INSTANCE ?: synchronized(lock = this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    klass = JournalJourneyDatabase::class.java,
                    name = "journal_journey_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = false) // Use only for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}