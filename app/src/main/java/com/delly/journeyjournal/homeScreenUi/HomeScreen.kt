package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.theme.Typography
import kotlinx.serialization.Serializable
import com.delly.journeyjournal.R as localR

@Serializable
object ActiveJournalsRoute

@Serializable
object CompletedJournalsRoute

/**
 * The main screen of the application. It displays a title, a button to create a new journey,
 * and two lists of journeys: active and complete.
 *
 * @param navToCreateEditJourneyScreen A lambda function to be invoked when the user clicks the button to create a new journey.
 * @param navigateToJourney A lambda function that takes a journey name as a string and navigates to that journey's screen.
 * @param repository The repository to fetch journey data from.
 */
@Composable
fun HomeScreen(
    navToCreateEditJourneyScreen: (Int?) -> Unit,
    navigateToJourney: (Int) -> Unit,
    repository: JournalRepository,
) {
    val allJourneys = repository.getAllJournals().collectAsState(initial = emptyList())
    val activeJourneys = allJourneys.value.filter { !it.isComplete }
    val completedJourneys = allJourneys.value.filter { it.isComplete }

    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Active", "Complete")

    Column(modifier = Modifier
        .padding(dimensionResource(id = localR.dimen.screen_edge_padding))
        .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = localR.string.home_title),
                style = Typography.headlineLarge
            )
        }

        HorizontalDivider()

        // Controls whether complete or incomplete journals are shown
        NavHost(
            navController = navController,
            startDestination = ActiveJournalsRoute,
            modifier = Modifier.weight(1f)
        ) {
            composable<ActiveJournalsRoute> {
                JournalsList(
                    journeys = activeJourneys,
                    repository = repository,
                    navigateToJourney = navigateToJourney,
                    onEditClick = { navToCreateEditJourneyScreen(it) },
                    header = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = localR.dimen.padding_small),
                                    bottom = dimensionResource(id = localR.dimen.padding_small)
                                ),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Create a new journal button
                            Button(
                                onClick = { navToCreateEditJourneyScreen(null) },
                                modifier = Modifier
                                    .height(dimensionResource(id = localR.dimen.button_height_mini))
                                    .width(dimensionResource(id = localR.dimen.button_height_mini)),
                                contentPadding = PaddingValues(dimensionResource(id = localR.dimen.button_internal_padding_zero))
                            ) {
                                Icon(
                                    modifier = Modifier.size(dimensionResource(id = localR.dimen.button_height_mini)),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(id = localR.string.add_journey)
                                )
                            }
                            Text(
                                stringResource(id = localR.string.active_journals),
                                style = Typography.titleMedium,
                                modifier = Modifier.padding(start = dimensionResource(id = localR.dimen.padding_small))
                            )
                        }
                    }
                )
            }
            composable<CompletedJournalsRoute> {
                JournalsList(
                    journeys = completedJourneys,
                    repository = repository,
                    navigateToJourney = navigateToJourney,
                    onEditClick = { navToCreateEditJourneyScreen(it) },
                    header = {
                        Text(
                            stringResource(id = localR.string.complete_journeys),
                            style = Typography.titleMedium,
                            modifier = Modifier.padding(dimensionResource(id = localR.dimen.padding_small))
                        )
                    }
                )
            }
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        if (index == 0) {
                            navController.navigate(ActiveJournalsRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
                            navController.navigate(CompletedJournalsRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    text = { Text(title) }
                )
            }
        }
    }
}
