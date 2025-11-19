package com.delly.journeyjournal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delly.journeyjournal.db.entities.JourneyEntryEntity

@Composable
fun JourneyEntryOverviewBox(
    entry: JourneyEntryEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Day Number and Distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Day ${entry.dayNumber}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${entry.distanceHiked} hiked",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Locations
            LabelValueRow(label = "Start:", value = entry.startLocation)
            LabelValueRow(label = "End:", value = entry.endLocation)

            Spacer(modifier = Modifier.height(8.dp))

            // Details Section
            if (entry.trailConditions.isNotBlank()) {
                LabelValueBlock(label = "Trail Conditions", value = entry.trailConditions)
            }

            if (entry.wildlifeSightings.isNotBlank()) {
                LabelValueBlock(label = "Wildlife", value = entry.wildlifeSightings)
            }

            if (entry.resupplyNotes.isNotBlank()) {
                LabelValueBlock(label = "Resupply / Water", value = entry.resupplyNotes)
            }

            if (entry.notes.isNotBlank()) {
                LabelValueBlock(label = "Notes", value = entry.notes)
            }
        }
    }
}

@Composable
private fun LabelValueRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = "$label ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LabelValueBlock(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
