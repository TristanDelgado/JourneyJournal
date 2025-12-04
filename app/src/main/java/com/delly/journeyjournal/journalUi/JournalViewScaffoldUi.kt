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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.enums.JourneyViewDestinations
import com.delly.journeyjournal.theme.Typography
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
fun JourneyViewScaffoldUi(
    navigateHome: () -> Unit,
    repository: JournalRepository,
    currentJournalId: Int
) {
    val localNavController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // TODO: Fetch journal name from ID
    var titleOfPage by remember { mutableStateOf(value = "Journey") }
    val journalId by remember { mutableStateOf(value = currentJournalId) }
    val coroutineScope = rememberCoroutineScope()

    // TODO: we should probably load the journal name here to display it in the title and side menu
    // For now passing empty string or placeholder to SideMenuUi
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenuUi(
                title = "Journey", // Placeholder until we load the name
                navigateHome = navigateHome
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
                            contentDescription = "Menu to open drawer",
                        )
                    }

                    // Title centered - takes up remaining space and centers text
                    Text(
                        text = titleOfPage,
                        style = Typography.headlineLarge,
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
                                selectedIndex = index
                                localNavController.navigate(route = destination.route) {
                                    // Clear back stack and navigate to single destination
                                    popUpTo(localNavController.graph.startDestinationId)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.contentDescription
                                )
                            },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(dimensionResource(localR.dimen.padding_normal))) {

                NavHost(
                    navController = localNavController,
                    startDestination = JourneyViewDestinations.ENTRIES.route, // Use the actual first destination route
                    modifier = Modifier.padding(paddingValues)
                ) {
                    JourneyViewDestinations.entries.forEach { destination ->
                        composable(destination.route) {
                            when (destination) {
                                JourneyViewDestinations.ENTRIES -> {
                                    JournalEntriesNav(
                                        repository,
                                        journalId
                                    )
                                    // titleOfPage = journalName // TODO: Fix title update
                                }

                                JourneyViewDestinations.MAP -> {
                                    UnderConstructionScreen()
                                    titleOfPage = "Map"
                                }

                                JourneyViewDestinations.STATS -> {
                                    UnderConstructionScreen()
                                    titleOfPage = "Travel Stats"
                                }

                                JourneyViewDestinations.FORECASTS -> {
                                    UnderConstructionScreen()
                                    titleOfPage = "Forecasts"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// TODO - Make this preview available by passing a repository into it.
//@Composable
//@Preview(showBackground = true)
//fun JourneyViewUiPreview() {
//    JourneyJournalTheme {
//        JourneyViewScaffoldUi(
//            navigateHome = { null },
//            repository = null,
//            currentJournalName = "Journey Name"
//        )
//    }
//}
