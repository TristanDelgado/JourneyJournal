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
    ),
}