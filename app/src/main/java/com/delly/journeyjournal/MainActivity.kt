package com.delly.journeyjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.delly.journeyjournal.db.JourneyJournalDatabase
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.theme.JourneyJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        val database = JourneyJournalDatabase.getDatabase(context = this)
        val journeyEntityDao = database.journeyEntityDao()
        val journeyEntryEntityDao = database.journeyEntryEntityDao()
        val journeyDao = database.journeyDao()
        val repository = JournalRepository(
            journeyEntityDao = journeyEntityDao,
            journeyEntryEntityDao = journeyEntryEntityDao,
            journeyDao = journeyDao
        )

        setContent {
            JourneyJournalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavHost(repository = repository)
                }
            }
        }
    }
}