package com.delly.journeyjournal.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.delly.journeyjournal.R

/**
 * Destinations
 *
 * @property route
 * @property labelResId
 * @property icon
 * @property contentDescriptionResId
 * @constructor Create empty Destinations
 */
enum class JourneyViewDestinations(
    val route: String,
    @StringRes val labelResId: Int,
    val icon: ImageVector,
    @StringRes val contentDescriptionResId: Int,
) {
    ENTRIES(
        route = "entries",
        labelResId = R.string.entries,
        Icons.Filled.Home,
        contentDescriptionResId = R.string.entries
    ),

    MAP(
        route = "map",
        labelResId = R.string.map,
        Icons.Filled.Person,
        contentDescriptionResId = R.string.map
    ),

    STATS(
        route = "stats",
        labelResId = R.string.stats,
        Icons.Filled.Settings,
        contentDescriptionResId = R.string.stats
    ),

    FORECASTS(
        route = "forecasts",
        labelResId = R.string.forecasts,
        Icons.Filled.Build,
        contentDescriptionResId = R.string.forecasts
    ),
}
