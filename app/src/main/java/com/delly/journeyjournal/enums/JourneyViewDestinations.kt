package com.delly.journeyjournal.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Destinations
 *
 * @property route
 * @property label
 * @property icon
 * @property contentDescription
 * @constructor Create empty Destinations
 */
enum class JourneyViewDestinations(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String,
) {
    ENTRIES(
        route = "entries",
        label = "Entries",
        Icons.Filled.Home,
        contentDescription = "entries"
    ),

    MAP(
        route = "map",
        label = "Map",
        Icons.Filled.Person,
        contentDescription = "map"
    ),

    STATS(
        route = "stats",
        label = "Stats",
        Icons.Filled.Settings,
        contentDescription = "stats"
    ),

    FORECASTS(
        route = "forecasts",
        label = "Forecasts",
        Icons.Filled.Build,
        contentDescription = "forecasts"
    ),
}