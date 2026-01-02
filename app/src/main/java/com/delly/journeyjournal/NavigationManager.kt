package com.delly.journeyjournal

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.homeScreenUi.CreateEditJourneyUi
import com.delly.journeyjournal.homeScreenUi.HomeScreen
import com.delly.journeyjournal.journalUi.JournalViewScaffoldUi
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class CreateEditJourney(val journalId: Long? = null)

@Serializable
data class SelectedJourney(val id: Long)

/**
 * Navigation graph for the app.
 */
@Composable
fun NavHost(
    repository: JournalRepository,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        // HomeScreen is the initial load in screen where all Journals are displayed
        composable<Home> {
            HomeScreen(
                navToCreateEditJournalScreen = { journalToEditId ->
                    navController.navigate(route = CreateEditJourney(journalId = journalToEditId))
                },
                navigateToJournal = { selectedJourneyId ->
                    navController.navigate(
                        route = SelectedJourney(
                            id = selectedJourneyId
                        )
                    )
                },
                repository = repository
            )
        }

        // CreateJourneyUi is used to create a new journey
        composable<CreateEditJourney> { backStackEntry ->
            val createJourney: CreateEditJourney = backStackEntry.toRoute()
            CreateEditJourneyUi(
                navigateHome = { navController.navigate(route = Home) },
                navigateToJourney = { selectedJourneyId ->
                    navController.navigate(
                        route = SelectedJourney(
                            id = selectedJourneyId
                        )
                    )
                },
                repository = repository,
                journalToEditId = createJourney.journalId
            )
        }

        // JourneyViewScaffoldUi is used to view the data for an existing journey
        composable<SelectedJourney> { selectedJourney ->
            val selectedJourneyData: SelectedJourney = selectedJourney.toRoute()

            JournalViewScaffoldUi(
                navigateHome = { navController.navigate(route = Home) },
                repository = repository,
                currentJournalId = selectedJourneyData.id,
            )
        }
    }
}
