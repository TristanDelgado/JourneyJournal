package com.delly.journeyjournal.journalUi

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

/**
 * Used to navigate viewing, creating and editing journal entries.
 *
 * @param repository The repository to get data from
 * @param journalId The id of the journal being viewed
 */
@Composable
fun JournalEntriesNav(
    repository: JournalRepository,
    journalId: Int
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
                journeyId = journalId
            )
        }

        // JourneyEntriesUi is the initial load in screen where all entries are displayed
        composable<CreateJourneyEntryDestination> {
            CreateJournalEntryUi(
                navigateBack = { navController.popBackStack() },
                repository = repository,
                journalId = journalId,
            )
        }
    }
}
