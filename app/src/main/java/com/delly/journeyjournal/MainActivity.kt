package com.delly.journeyjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.delly.journeyjournal.db.JournalJourneyDatabase
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        val database = JournalJourneyDatabase.getDatabase(context = this)
        val userDao = database.journeyEntityDao()
        val repository = JournalRepository(journeyEntityDao = userDao)

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