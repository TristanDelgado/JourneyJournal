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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import com.delly.journeyjournal.R as localR

@Serializable
object ActiveJournalsRoute

@Serializable
object CompletedJournalsRoute

/**
 * The main screen of the application. It displays a title, a button to create a new journal,
 * and two lists of journals: active and complete.
 *
 * @param navToCreateEditJournalScreen A lambda function to be invoked when the user clicks the button to create a new journal.
 * @param navigateToJournal A lambda function that takes a journal name as a string and navigates to that journal's screen.
 * @param repository The repository to fetch journal data from.
 */
@Composable
fun HomeScreen(
    navToCreateEditJournalScreen: (Int?) -> Unit,
    navigateToJournal: (Int) -> Unit,
    repository: JournalRepository,
) {
    val allJournalsWithEntries = repository.getAllJournalsWithEntries()
        .collectAsState(initial = emptyList())

    // This calculation now only happens when 'allJournalsWithEntries' changes
    val activeJournals = remember(allJournalsWithEntries.value) {
        allJournalsWithEntries.value.filter { !it.journal.isComplete }
    }

    val completedJournals = remember(allJournalsWithEntries.value) {
        allJournalsWithEntries.value.filter { it.journal.isComplete }
    }

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Active",
        "Complete"
    )

    Column(
        modifier = Modifier
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
                    journals = activeJournals,
                    navigateToJournal = navigateToJournal,
                    onEditClick = { navToCreateEditJournalScreen(it) },
                    onDeleteClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            repository.deleteJournal(it.journal)
                        }
                    },
                    onSettingsClick = {},
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
                                onClick = { navToCreateEditJournalScreen(null) },
                                modifier = Modifier
                                    .height(dimensionResource(id = localR.dimen.button_height_mini))
                                    .width(dimensionResource(id = localR.dimen.button_height_mini)),
                                contentPadding = PaddingValues(dimensionResource(id = localR.dimen.button_internal_padding_zero))
                            ) {
                                Icon(
                                    modifier = Modifier.size(dimensionResource(id = localR.dimen.button_height_mini)),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(id = localR.string.add_journal)
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
                    journals = completedJournals,
                    navigateToJournal = navigateToJournal,
                    onEditClick = { navToCreateEditJournalScreen(it) },
                    onDeleteClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            repository.deleteJournal(it.journal)
                        }
                    },
                    onSettingsClick = {},
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
                                onClick = { navToCreateEditJournalScreen(null) },
                                modifier = Modifier
                                    .height(dimensionResource(id = localR.dimen.button_height_mini))
                                    .width(dimensionResource(id = localR.dimen.button_height_mini)),
                                contentPadding = PaddingValues(dimensionResource(id = localR.dimen.button_internal_padding_zero))
                            ) {
                                Icon(
                                    modifier = Modifier.size(dimensionResource(id = localR.dimen.button_height_mini)),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(id = localR.string.add_journal)
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
