package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.db.entities.JournalWithEntries
import com.delly.journeyjournal.enums.DistanceUnit
import com.delly.journeyjournal.enums.TransportationMethods
import com.delly.journeyjournal.theme.JourneyJournalTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.delly.journeyjournal.R as localR

/**
 * A box that displays basic overall information about a journal.
 * Updated to match the styling of JournalEntryOverviewBox.
 */
@Composable
fun JournalOverviewBox(
    journalWithEntries: JournalWithEntries,
    navigateToJournal: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (JournalWithEntries) -> Unit,
    onSettingsClick: (Long) -> Unit,
) {
    val journal = journalWithEntries.journal
    val entries = journalWithEntries.entries

    // Calculations
    val totalDistance = remember(entries) {
        entries.sumOf { it.distanceHiked.toDoubleOrNull() ?: 0.0 }
    }

    val lastEntryDate = remember(entries) {
        entries.maxByOrNull { it.date }?.date
    }

    // Date Formatter (You might want to move this to a utility class)
    fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return "N/A"
        val sdf = SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.getDefault()
        )
        return sdf.format(Date(timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp, // Standard padding
                vertical = 4.dp
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { navigateToJournal(journal.id) },
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
                // Journal Name and Description
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = journal.journalName.ifBlank { stringResource(id = localR.string.untitled_journal) },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = journal.courseName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // Action Buttons (Edit, Delete, Settings)
                Row(horizontalArrangement = Arrangement.End) {
                    ActionIcon(
                        icon = Icons.Default.Edit,
                        contentDescription = stringResource(id = localR.string.edit_journal),
                        onClick = { onEditClick(journal.id) }
                    )
                    ActionIcon(
                        icon = Icons.Default.Delete,
                        contentDescription = stringResource(id = localR.string.delete_journal),
                        onClick = { onDeleteClick(journalWithEntries) }
                    )
                    ActionIcon(
                        icon = Icons.Default.Settings,
                        contentDescription = stringResource(id = localR.string.journal_settings),
                        onClick = { onSettingsClick(journal.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Stats Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total Distance
                StatBlock(
                    label = if (journal.distanceUnit == DistanceUnit.MILES) {
                        stringResource(id = localR.string.total_miles)
                    } else {
                        stringResource(id = localR.string.total_kilometers)
                    },
                    value = String.format(
                        "%.1f",
                        totalDistance
                    )
                )

                // Last Entry Date
                StatBlock(
                    label = stringResource(id = localR.string.last_entry),
                    value = formatDate(lastEntryDate),
                    alignment = Alignment.End
                )
            }
        }
    }
}

@Composable
private fun ActionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(32.dp) // Slightly smaller than default to fit 3 in a row
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
@Preview(showBackground = true)
fun JournalOverviewBoxPreview() {
    val journalEntity = JournalEntity(
        id = 1,
        journalName = "Appalachian Trail 2025",
        journeymanName = "Hiker Joe",
        courseName = "Appalachian Trail",
        courseRegion = "East Coast",
        startDate = 1704067200000L, // Jan 1 2024
        transportationMethod = TransportationMethods.ON_FOOT,
        description = "A long walk.",
        distanceUnit = DistanceUnit.MILES
    )

    val entries = listOf(
        JournalEntryEntity(
            ownerId = 1,
            date = 1704153600000L,
            dayNumber = "1",
            startLocation = "Springer",
            endLocation = "Shelter",
            distanceHiked = "8.5",
            trailConditions = "",
            wildlifeSightings = "",
            resupplyNotes = "",
            notes = "",
            startMileMarker = "",
            endMileMarker = "",
            elevationStart = "",
            elevationEnd = "",
            netElevationChange = "",
            sleptInBed = false,
            tookShower = false,
            weather = "",
            dayRating = "",
            moodRating = ""
        ),
        JournalEntryEntity(
            ownerId = 1,
            date = 1704240000000L,
            dayNumber = "2",
            startLocation = "Shelter",
            endLocation = "Town",
            distanceHiked = "12.2",
            trailConditions = "",
            wildlifeSightings = "",
            resupplyNotes = "",
            notes = "",
            startMileMarker = "",
            endMileMarker = "",
            elevationStart = "",
            elevationEnd = "",
            netElevationChange = "",
            sleptInBed = false,
            tookShower = false,
            weather = "",
            dayRating = "",
            moodRating = ""
        )
    )

    val journalWithEntries = JournalWithEntries(
        journal = journalEntity,
        entries = entries
    )

    JourneyJournalTheme {
        JournalOverviewBox(
            journalWithEntries = journalWithEntries,
            navigateToJournal = { },
            onEditClick = { },
            onDeleteClick = { },
            onSettingsClick = { }
        )
    }
}
