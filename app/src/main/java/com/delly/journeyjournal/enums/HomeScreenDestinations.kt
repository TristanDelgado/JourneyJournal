package com.delly.journeyjournal.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.delly.journeyjournal.R

enum class HomeScreenDestinations(
    val route: String,
    @StringRes val labelResId: Int,
    val icon: ImageVector,
    @StringRes val contentDescriptionResId: Int,
) {
    ACTIVE(
        route = "active",
        labelResId = R.string.active_journals_nav,
        icon = Icons.Default.Home,
        contentDescriptionResId = R.string.active_journals_content_description
    ),
    COMPLETED(
        route = "completed",
        labelResId = R.string.completed_journals_nav,
        icon = Icons.Default.CheckCircle,
        contentDescriptionResId = R.string.completed_journals_content_description
    )
}
