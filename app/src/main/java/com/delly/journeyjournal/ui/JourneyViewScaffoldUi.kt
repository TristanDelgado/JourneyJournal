package com.delly.journeyjournal.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.enums.Destinations
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme
import com.delly.journeyjournal.ui.theme.Typography
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyViewScaffoldUi(
    navigateHome: () -> Unit,
    repository: JournalRepository?,
    currentJournalName: String
) {
    val localNavController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var titleOfPage by remember { mutableStateOf(value = currentJournalName) }
    val journalName by remember { mutableStateOf(value = currentJournalName) }
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenuUi(
                title = journalName,
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
                    Destinations.entries.forEachIndexed { index, destination ->
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
                    startDestination = Destinations.ENTRIES.route, // Use the actual first destination route
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Destinations.entries.forEach { destination ->
                        composable(destination.route) {
                            when (destination) {
                                Destinations.ENTRIES -> {
                                    JourneyEntriesUi()
                                    titleOfPage = journalName
                                }

                                Destinations.MAP -> {
                                    UnderConstructionScreen()
                                    titleOfPage = "Map"
                                }

                                Destinations.STATS -> {
                                    UnderConstructionScreen()
                                    titleOfPage = "Travel Stats"
                                }

                                Destinations.FORECASTS -> {
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

@Composable
@Preview(showBackground = true)
fun JourneyViewUiPreview() {
    JourneyJournalTheme {
        JourneyViewScaffoldUi(
            navigateHome = { null },
            repository = null,
            currentJournalName = "Journey Name"
        )
    }
}