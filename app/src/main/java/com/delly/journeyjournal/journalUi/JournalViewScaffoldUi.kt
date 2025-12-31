package com.delly.journeyjournal.journalUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.enums.JourneyViewDestinations
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.viewmodels.JournalViewViewModel
import com.delly.journeyjournal.viewmodels.JournalViewViewModelFactory
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

/**
 * This composable is the main UI for viewing a journey. It sets up a `ModalNavigationDrawer` with a
 * `Scaffold`. The `Scaffold` has a top bar, a bottom navigation bar, and the main content area which
 * is a `NavHost`.
 *
 * @param navigateHome A lambda to navigate back to the home screen.
 * @param repository The repository to get journal data from.
 * @param currentJournalId The id of the currently selected journal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalViewScaffoldUi(
    navigateHome: () -> Unit,
    repository: JournalRepository,
    currentJournalId: Int,
) {
    val viewModel: JournalViewViewModel = viewModel(
        factory = JournalViewViewModelFactory(
            repository,
            currentJournalId,
        )
    )

    // Observing ViewModel State
    val titleOfPage by viewModel.titleOfPage.collectAsState()
    val selectedIndex by viewModel.selectedIndex.collectAsState()
    val currentJournal by viewModel.currentJournal.collectAsState()

    val localNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenuUi(
                title = titleOfPage,
                isComplete = currentJournal?.isComplete ?: false,
                navigateHome = navigateHome,
                invertCompleteStatus = {
                    viewModel.invertCompleteStatus()
                },
                closeDrawer = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
        },
    )
    {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(localR.dimen.status_bar_height))
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(dimensionResource(localR.dimen.padding_normal)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Menu button on the left
                    IconButton(onClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }) {
                        Icon(
                            modifier = Modifier
                                .padding(dimensionResource(localR.dimen.padding_small))
                                .size(dimensionResource(localR.dimen.large_button_height)),
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = localR.string.menu_open_drawer),
                        )
                    }

                    // Title centered
                    Text(
                        text = titleOfPage,
                        style = Typography.headlineMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 48.dp), // Compensate for menu button width
                        textAlign = TextAlign.Center
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    JourneyViewDestinations.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                viewModel.updateSelectedIndex(index)
                                localNavController.navigate(route = destination.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(localNavController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // re-selecting the same item
                                    launchSingleTop = true
                                    // Restore state when re-selecting a previously selected item
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = stringResource(id = destination.contentDescriptionResId)
                                )
                            },
                            label = { Text(stringResource(id = destination.labelResId)) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(dimensionResource(localR.dimen.padding_normal))) {

                NavHost(
                    navController = localNavController,
                    startDestination = JourneyViewDestinations.ENTRIES.route,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    JourneyViewDestinations.entries.forEach { destination ->
                        composable(destination.route) {
                            when (destination) {
                                JourneyViewDestinations.ENTRIES -> {
                                    JournalEntriesNav(
                                        repository,
                                        currentJournalId
                                    )
                                    viewModel.updateTitle(
                                        currentJournal?.journalName?.ifBlank { stringResource(id = localR.string.untitled_journal) }
                                            ?: stringResource(id = localR.string.loading)
                                    )
                                }

                                JourneyViewDestinations.MAP -> {
                                    UnderConstructionScreen()
                                    viewModel.updateTitle(stringResource(id = localR.string.map))
                                }

                                JourneyViewDestinations.STATS -> {
                                    UnderConstructionScreen()
                                    viewModel.updateTitle(stringResource(id = localR.string.travel_stats))
                                }

                                JourneyViewDestinations.FORECASTS -> {
                                    UnderConstructionScreen()
                                    viewModel.updateTitle(stringResource(id = localR.string.forecasts))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// TODO: Make this preview work
//@Composable
//@Preview(showBackground = true)
//fun JourneyViewUiPreview() {
//    val journalEntity = JournalEntity(
//        id = 1,
//        journalName = "Appalachian Trail 2025",
//        journeymanName = "Hiker Joe",
//        courseName = "Appalachian Trail",
//        courseRegion = "East Coast",
//        startDate = 1704067200000L, // Jan 1 2024
//        transportationMethod = TransportationMethods.ON_FOOT,
//        description = "A long walk."
//    )
//
//    val entries = listOf(
//        JournalEntryEntity(
//            ownerId = 1,
//            date = 1704153600000L,
//            dayNumber = "1",
//            startLocation = "Springer",
//            endLocation = "Shelter",
//            distanceHiked = "8.5",
//            trailConditions = "", wildlifeSightings = "", resupplyNotes = "", notes = "",
//            startMileMarker = "", endMileMarker = "", elevationStart = "", elevationEnd = "",
//            netElevationChange = "", sleptInBed = false, tookShower = false,
//            weather = "", dayRating = "", moodRating = ""
//        ),
//        JournalEntryEntity(
//            ownerId = 1,
//            date = 1704240000000L,
//            dayNumber = "2",
//            startLocation = "Shelter",
//            endLocation = "Town",
//            distanceHiked = "12.2",
//            trailConditions = "", wildlifeSightings = "", resupplyNotes = "", notes = "",
//            startMileMarker = "", endMileMarker = "", elevationStart = "", elevationEnd = "",
//            netElevationChange = "", sleptInBed = false, tookShower = false,
//            weather = "", dayRating = "", moodRating = ""
//        )
//    )
//
//    val journalWithEntries = JournalWithEntries(
//        journal = journalEntity,
//        entries = entries
//    )
//
//    JourneyJournalTheme {
//        JournalViewScaffoldUi(
//            navigateHome = { },
//            repository = journalWithEntries,
//            currentJournalId = 0
//        )
//    }
//}
