package com.delly.journeyjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.db.JournalJourneyDatabase
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.ui.CreateJourneyUi
import com.delly.journeyjournal.ui.HomeScreen
import com.delly.journeyjournal.ui.JourneyEntriesUi
import com.delly.journeyjournal.ui.JourneyViewScaffoldUi
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        val database = JournalJourneyDatabase.getDatabase(context = this)
        val userDao = database.journeyEntityDao()
        val repository = JournalRepository(userDao)

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

/**
 * Navigation graph for the app.
 */
@Composable
fun NavHost(repository: JournalRepository) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController, repository) }
        composable("journeyView") { JourneyViewScaffoldUi(navController, repository) }
        composable("createJourney") { CreateJourneyUi(navController, repository) }
        composable("journeyEntries") { JourneyEntriesUi() }
        // Add other destinations
    }
}