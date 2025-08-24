package com.delly.journeyjournal.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme
import com.delly.journeyjournal.R as localR

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String,
) {
    ENTRIES(
        "entries",
        "Entries",
        Icons.Filled.Home,
        "entries"
    ),
    MAP(
        "map",
        "Map",
        Icons.Filled.Person,
        "map"
    ),
    STATS(
        "stats",
        "Stats",
        Icons.Filled.Settings,
        "stats"
    ),
    FORECASTS(
        "forecasts",
        "Forecasts",
        Icons.Filled.Build,
        "forecasts"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyViewScaffoldUi(mainNavController: NavController) {
    val localNavController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
//        topBar = {
//            Button(onClick = )
//        },
        bottomBar = {
            NavigationBar {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            localNavController.navigate(route = destination.route) {
                                // Clear back stack and navigate to single destination
                                popUpTo(localNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
                startDestination = Destination.ENTRIES.route, // Use the actual first destination route
                modifier = Modifier.padding(paddingValues)
            ) {
                Destination.entries.forEach { destination ->
                    composable(destination.route) {
                        when (destination) {
                            Destination.ENTRIES -> JourneyEntriesUi()
                            Destination.MAP -> UnderConstructionScreen("Map")
                            Destination.STATS -> UnderConstructionScreen("Travel Stats")
                            Destination.FORECASTS -> UnderConstructionScreen("Forecasts")
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

    val mockNavController = rememberNavController()

    JourneyJournalTheme {
        JourneyViewScaffoldUi(mockNavController)
    }
}