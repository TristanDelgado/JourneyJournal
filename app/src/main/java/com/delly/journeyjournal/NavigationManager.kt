package com.delly.journeyjournal

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.ui.CreateJourneyUi
import com.delly.journeyjournal.ui.HomeScreen
import com.delly.journeyjournal.ui.JourneyViewScaffoldUi
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object CreateJourney

@Serializable
data class SelectedJourney(val name: String)

/**
 * Navigation graph for the app.
 */
@Composable
fun NavHost(
    repository: JournalRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        // HomeScreen is the initial load in screen where all Journals are displayed
        //composable(route = "home") { HomeScreen(navController, repository) }
        composable<Home> {
            HomeScreen(
                navToCreateJourneyScreen = { navController.navigate(route = CreateJourney) },
                repository = repository
            )
        }

        // CreateJourneyUi is used to create a new journey
        //composable(route = "createJourney", arguments = ) { CreateJourneyUi(navController, repository) }
        composable<CreateJourney> {
            CreateJourneyUi(
                navigateHome = { navController.navigate(route = Home) },
                navigateToJourney = { selectedJourney ->
                    navController.navigate(
                        route = SelectedJourney(
                            name = selectedJourney
                        )
                    )
                },
                repository = repository
            )
        }

        // JourneyViewScaffoldUi is used to view a specific Journal
//        composable(route = "journeyView") {
//            JourneyViewScaffoldUi(
//                mainNavController = navController,
//                repository = repository,
//                currentJourney = journeyName
//            )
//        }
        composable<SelectedJourney> { selectedJourney ->
            val selectedJourneyName: SelectedJourney = selectedJourney.toRoute()

            JourneyViewScaffoldUi(
                navigateHome = { navController.navigate(route = Home) },
                repository = repository,
                currentJournalName = selectedJourneyName.name,
            )
        }
    }
}