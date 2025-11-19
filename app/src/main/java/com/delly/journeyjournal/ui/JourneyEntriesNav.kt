package com.delly.journeyjournal.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.db.JournalRepository
import kotlinx.serialization.Serializable

@Serializable
object JourneyEntriesDestination

@Serializable
object CreateJourneyEntryDestination

@Composable
fun JourneyEntriesNav(
    repository: JournalRepository,
    journeyName: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = JourneyEntriesDestination
    ) {
        // JourneyEntriesUi is the initial load in screen where all entries are displayed
        composable<JourneyEntriesDestination> {
            JourneyEntriesUi(
                navigateToCreateEntry = { navController.navigate(CreateJourneyEntryDestination) },
                repository = repository,
                journeyName = journeyName
            )
        }

        // JourneyEntriesUi is the initial load in screen where all entries are displayed
        composable<CreateJourneyEntryDestination> {
            CreateJourneyEntryUi(
                navigateBack = { navController.popBackStack() },
                repository = repository,
                journeyId = journeyName,
            )
        }
    }
}
