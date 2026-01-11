package com.delly.journeyjournal.journalUi

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.delly.journeyjournal.journalUi.entries.JournalEntriesNav
import com.delly.journeyjournal.journalUi.forecasts.ForecastsScreenUi
import com.delly.journeyjournal.journalUi.stats.StatsScreenUi
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.viewmodels.JournalViewViewModel
import com.delly.journeyjournal.viewmodels.JournalViewViewModelFactory
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

/**
 * This composable is the main UI for viewing a journey. It sets up a `ModalNavigationDrawer` with a
 * `Scaffold`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalViewScaffoldUi(
    navigateHome: () -> Unit,
    repository: JournalRepository,
    currentJournalId: Long,
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

    // --- State Freezing Logic ---
    // 1. Get the "Live" state from the DB
    val liveCompleteState = currentJournal?.isComplete ?: false

    // 2. Create local state to "Freeze" the UI when leaving
    var isFrozen by remember { mutableStateOf(false) }
    var frozenCompleteState by remember { mutableStateOf(liveCompleteState) }

    // 3. Decide which state to show:
    // If frozen, show the snapshot. If not, show live data.
    val displayedCompleteState = if (isFrozen) frozenCompleteState else liveCompleteState

    val localNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenuUi(
                title = titleOfPage,
                isComplete = displayedCompleteState, // Pass the controlled state
                navigateHome = navigateHome,
                markCompleteAndNavHome = {
                    // A. Snapshot the current state and Freeze it
                    frozenCompleteState = liveCompleteState
                    isFrozen = true

                    // B. Navigate away
                    navigateHome()

                    // C. Update DB (The UI will ignore this change now)
                    viewModel.invertCompleteStatus()
                },
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
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        ),
                    title = {
                        Text(
                            text = titleOfPage,
                            style = Typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
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
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
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
                                StatsScreenUi(
                                    repository,
                                    currentJournalId
                                )
                                viewModel.updateTitle(stringResource(id = localR.string.travel_stats))
                            }

                            JourneyViewDestinations.FORECASTS -> {
                                ForecastsScreenUi(
                                    repository = repository,
                                    journalId = currentJournalId
                                )
                                viewModel.updateTitle(stringResource(id = localR.string.forecasts))
                            }
                        }
                    }
                }
            }
        }
    }
}
