package com.delly.journeyjournal.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.delly.journeyjournal.db.dataAccessObjects.JourneyEntityDao
import com.delly.journeyjournal.db.entities.JourneyEntity

@Database(
    entities = [JourneyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class JournalJourneyDatabase : RoomDatabase() {

    abstract fun journeyEntityDao(): JourneyEntityDao

    companion object {
        @Volatile
        private var INSTANCE: JournalJourneyDatabase? = null

        fun getDatabase(context: Context): JournalJourneyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JournalJourneyDatabase::class.java,
                    "journal_journey_database"
                )
                    .fallbackToDestructiveMigration() // Use only for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}