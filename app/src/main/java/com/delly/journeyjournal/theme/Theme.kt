package com.delly.journeyjournal.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable

/**
 * Journey journal theme
 *
 * @param content The content to apply the theme to.
 */
@Composable
fun JourneyJournalTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}