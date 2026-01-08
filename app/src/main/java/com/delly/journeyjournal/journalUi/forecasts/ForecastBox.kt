package com.delly.journeyjournal.journalUi.forecasts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A Card component that displays details about a specific [Forecast] item.
 *
 * This composable renders a card containing:
 * - The name of the forecast location (e.g., "Harper's Ferry").
 * - Action buttons to Edit or Delete the forecast.
 * - A row of key statistics: Mile Marker, Estimated Days Away, and Estimated Arrival Date.
 *
 * @param forecast The [Forecast] data object containing the values to display.
 * @param onEditClick Callback triggered when the edit (pencil) button is clicked.
 * @param onDeleteClick Callback triggered when the delete (trash can) button is clicked.
 */
@Composable
fun ForecastBox(
    forecast: Forecast,
    onEditClick: (Forecast) -> Unit,
    onDeleteClick: (Forecast) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Name + Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = forecast.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f) // Push icons to the right
                )

                // Action Buttons
                Row {
                    IconButton(onClick = { onEditClick(forecast) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { onDeleteClick(forecast) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error // Make delete button red-ish
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Stat(
                    label = "Mile Marker",
                    value = forecast.mileMarker.toString(),
                    icon = Icons.Default.Flag
                )
                Stat(
                    label = "Days Away",
                    value = forecast.estimatedDaysToArrival?.toString() ?: "--",
                    icon = Icons.Default.Timeline
                )
                Stat(
                    label = "Arrival Date",
                    value = forecast.estimatedArrivalDate ?: "--",
                    icon = Icons.Default.CalendarToday
                )
            }
        }
    }
}

/**
 * A helper composable that renders a single statistic column within the Forecast card.
 *
 * Displays an icon on top, followed by the value, and finally a label description at the bottom.
 *
 * @param label The descriptive text under the value (e.g., "Days Away").
 * @param value The main data string to display (e.g., "5").
 * @param icon The [ImageVector] icon to display above the value.
 */
@Composable
private fun Stat(
    label: String,
    value: String,
    icon: ImageVector,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
