package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalWithEntries
import com.delly.journeyjournal.enums.HomeScreenDestinations
import com.delly.journeyjournal.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

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
    navToCreateEditJournalScreen: (Long?) -> Unit,
    navigateToJournal: (Long) -> Unit,
    repository: JournalRepository,
) {
    val allJournalsWithEntries = repository.getAllJournalsWithEntries()
        .collectAsState(initial = emptyList())

    // This calculation now only happens when 'allJournalsWithEntries' changes
    val activeJournals = remember(allJournalsWithEntries.value) {
        allJournalsWithEntries.value.filter { !it.journal.isComplete }
    }

    val completeJournals = remember(allJournalsWithEntries.value) {
        allJournalsWithEntries.value.filter { it.journal.isComplete }
    }

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    var journalToDelete by remember { mutableStateOf<JournalWithEntries?>(null) }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                HomeScreenDestinations.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination?.route == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = stringResource(id = destination.contentDescriptionResId)
                            )
                        },
                        label = { Text(stringResource(id = destination.labelResId)) }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navToCreateEditJournalScreen(null) }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = localR.string.add_journal)
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
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
                startDestination = HomeScreenDestinations.ACTIVE.route,
                modifier = Modifier.weight(1f)
            ) {
                composable(HomeScreenDestinations.ACTIVE.route) {
                    JournalsList(
                        journals = activeJournals,
                        navigateToJournal = navigateToJournal,
                        onEditClick = { navToCreateEditJournalScreen(it) },
                        onDeleteClick = { journalToDelete = it },
                        onSettingsClick = { },
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
                                Text(
                                    stringResource(id = localR.string.active_journals),
                                    style = Typography.titleMedium,
                                    modifier = Modifier.padding(start = dimensionResource(id = localR.dimen.padding_small))
                                )
                            }
                        },
                        emptyState = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = localR.string.no_active_journals),
                                    style = Typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    )
                }
                composable(HomeScreenDestinations.COMPLETE.route) {
                    JournalsList(
                        journals = completeJournals,
                        navigateToJournal = navigateToJournal,
                        onEditClick = { navToCreateEditJournalScreen(it) },
                        onDeleteClick = { journalToDelete = it },
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
                                Text(
                                    text = stringResource(id = localR.string.complete_journals),
                                    style = Typography.titleMedium,
                                    modifier = Modifier.padding(start = dimensionResource(id = localR.dimen.padding_small))
                                )
                            }
                        },
                        emptyState = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = localR.string.no_complete_journals),
                                    style = Typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    journalToDelete?.let { journal ->
        // ... inside your Composable where the dialog is shown

        journalToDelete?.let { journal ->
            // State to track the countdown (Starts at 3 seconds)
            var timeLeft by remember { mutableIntStateOf(3) }

            // Start the countdown when this dialog enters the composition
            LaunchedEffect(Unit) {
                while (timeLeft > 0) {
                    kotlinx.coroutines.delay(1000L)
                    timeLeft--
                }
            }

            AlertDialog(
                onDismissRequest = { journalToDelete = null },
                title = { Text(text = "Delete Journal") },
                text = { Text("Are you sure you want to delete the \"${journal.journal.journalName}\" journal? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        // Disable the button while the timer is running
                        enabled = timeLeft == 0,

                        // Make the button red to indicate a destructive action
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors( // Changed to textButtonColors since you are using TextButton
                            containerColor = if (timeLeft == 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            contentColor = if (timeLeft == 0) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        ),
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                journalToDelete?.let { repository.deleteJournal(it.journal) }
                                journalToDelete = null
                            }
                        },
                    ) {
                        // Update text to visually show the countdown
                        Text(if (timeLeft > 0) "Delete ($timeLeft)" else "Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { journalToDelete = null }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
