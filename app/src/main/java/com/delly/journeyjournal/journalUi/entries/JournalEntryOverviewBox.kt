package com.delly.journeyjournal.journalUi.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.enums.DistanceUnit
import com.delly.journeyjournal.theme.JourneyJournalTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.delly.journeyjournal.R as localR

/**
 * Journey entry overview box used to see the general overview of an entry in a journal.
 * Updated to match the styling of JournalOverviewBox.
 */
@Composable
fun JournalEntryOverviewBox(
    entry: JournalEntryEntity,
    distanceUnit: DistanceUnit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (JournalEntryEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Date Formatter
    val formattedDate = remember(entry.date) {
        val sdf = SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.getDefault()
        )
        sdf.format(Date(entry.date))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- Header Row: Title and Action Buttons ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Left Side: Day Number and Locations
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(
                            id = localR.string.day_format,
                            entry.dayNumber
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    // Combining Start -> End into a subtitle
                    Text(
                        text = "${entry.startLocation} ➔ ${entry.endLocation}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // Right Side: Action Buttons (Edit, Delete)
                Row(horizontalArrangement = Arrangement.End) {
                    ActionIcon(
                        icon = Icons.Default.Edit,
                        contentDescription = stringResource(id = localR.string.edit_journal),
                        onClick = { onEditClick(entry.id) }
                    )
                    ActionIcon(
                        icon = Icons.Default.Delete,
                        contentDescription = stringResource(id = localR.string.delete_journal),
                        onClick = { onDeleteClick(entry) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Stats Row (Distance, Elevation, Date) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Group: Distance + Elevation
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatBlock(
                        label = stringResource(id = localR.string.distance_label),
                        value = stringResource(
                            id = if (distanceUnit == DistanceUnit.MILES) {
                                localR.string.miles_format
                            } else {
                                localR.string.kilometers_format
                            },
                            entry.distanceHiked
                        )
                    )

                    // Elevation Change (If present)
                    if (entry.netElevationChange.isNotBlank()) {
                        Spacer(modifier = Modifier.width(24.dp)) // Space between Distance and Elevation
                        StatBlock(
                            label = stringResource(id = localR.string.elevation_label), // Needs "Elevation" or "Net Elev."
                            value = entry.netElevationChange
                        )
                    }
                }

                // Right Group: Date
                StatBlock(
                    label = stringResource(id = localR.string.date_label),
                    value = formattedDate,
                    alignment = Alignment.End
                )
            }

            // --- Conditional Details Section ---
            // Check if ANY detailed info exists (Ratings, Amenities, Conditions, Notes)
            val hasRatings =
                entry.weather.isNotBlank() || entry.dayRating.isNotBlank() || entry.moodRating.isNotBlank()
            val hasAmenities = entry.sleptInBed || entry.tookShower
            val hasTextDetails =
                entry.trailConditions.isNotBlank() || entry.wildlifeSightings.isNotBlank() ||
                        entry.resupplyNotes.isNotBlank() || entry.notes.isNotBlank()

            if (hasRatings || hasAmenities || hasTextDetails) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 1. Ratings Row (Weather, Mood, Rating)
                if (hasRatings) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (entry.weather.isNotBlank()) {
                            StatBlock(
                                label = stringResource(id = localR.string.weather_label),
                                value = entry.weather
                            )
                        }
                        if (entry.dayRating.isNotBlank()) {
                            StatBlock(
                                label = stringResource(id = localR.string.day_rating_label),
                                value = entry.dayRating
                            )
                        }
                        if (entry.moodRating.isNotBlank()) {
                            StatBlock(
                                label = stringResource(id = localR.string.mood_rating_label),
                                value = entry.moodRating
                            )
                        }
                    }
                }

                // 2. Amenities (Bed / Shower)
                if (hasAmenities) {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between badges
                        ) {
                            if (entry.sleptInBed) {
                                AmenityBadge(
                                    icon = Icons.Default.Hotel,
                                    text = "Slept in Bed"
                                )
                            }
                            if (entry.tookShower) {
                                AmenityBadge(
                                    icon = Icons.Default.Shower,
                                    text = "Took Shower"
                                )
                            }
                        }
                    }
                }

                // 3. Text Details
                if (entry.trailConditions.isNotBlank()) {
                    DetailBlock(
                        label = stringResource(id = localR.string.trail_conditions),
                        value = entry.trailConditions
                    )
                }

                if (entry.wildlifeSightings.isNotBlank()) {
                    DetailBlock(
                        label = stringResource(id = localR.string.wildlife_label),
                        value = entry.wildlifeSightings
                    )
                }

                if (entry.resupplyNotes.isNotBlank()) {
                    DetailBlock(
                        label = stringResource(id = localR.string.resupply_water_label),
                        value = entry.resupplyNotes
                    )
                }

                if (entry.notes.isNotBlank()) {
                    DetailBlock(
                        label = stringResource(id = localR.string.notes_label),
                        value = entry.notes
                    )
                }
            }
        }
    }
}

// --- Helper Composables ---

@Composable
private fun ActionIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(32.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AmenityBadge(
    icon: ImageVector,
    text: String,
) {
    Surface(
        shape = MaterialTheme.shapes.small, // Rounded corners
        color = MaterialTheme.colorScheme.secondaryContainer, // Subtle highlight color
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun StatBlock(
    label: String,
    value: String,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(horizontalAlignment = alignment) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DetailBlock(
    label: String,
    value: String,
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// --- Preview ---

@Preview(showBackground = true)
@Composable
fun JournalEntryOverviewBoxPreview() {
    val entry = JournalEntryEntity(
        id = 1,
        ownerId = 1,
        date = 1704153600000L, // Example Timestamp
        dayNumber = "5",
        startLocation = "Deep Gap Shelter",
        endLocation = "Dicks Creek Gap",
        distanceHiked = "12.4",
        trailConditions = "Muddy and slick after the rain.",
        wildlifeSightings = "Saw a black bear cub!",
        resupplyNotes = "",
        notes = "Felt strong today, but knees hurt on the descent.",
        startMileMarker = "",
        endMileMarker = "",
        elevationStart = "",
        elevationEnd = "",
        netElevationChange = "+2400 ft", // Added for preview
        sleptInBed = true, // Added for preview
        tookShower = true, // Added for preview
        weather = "Rainy",
        dayRating = "4/5",
        moodRating = "Happy"
    )

    JourneyJournalTheme {
        JournalEntryOverviewBox(
            entry = entry,
            distanceUnit = DistanceUnit.MILES,
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
