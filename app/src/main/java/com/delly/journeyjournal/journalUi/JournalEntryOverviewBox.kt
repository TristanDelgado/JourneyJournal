package com.delly.journeyjournal.journalUi

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.R as localR

/**
 * Journey entry overview box used to see the general overview of an entry in a journal.
 *
 * @param entry The entry to display
 * @param modifier
 */
@Composable
fun JournalEntryOverviewBox(
    entry: JournalEntryEntity,
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
                    text = stringResource(id = localR.string.day_format, entry.dayNumber),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = localR.string.hiked_format, entry.distanceHiked),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Locations
            LabelValueRow(label = stringResource(id = localR.string.start_label), value = entry.startLocation)
            LabelValueRow(label = stringResource(id = localR.string.end_label), value = entry.endLocation)

            Spacer(modifier = Modifier.height(8.dp))

            // Details Section
            if (entry.trailConditions.isNotBlank()) {
                LabelValueBlock(label = stringResource(id = localR.string.trail_conditions), value = entry.trailConditions)
            }

            if (entry.wildlifeSightings.isNotBlank()) {
                LabelValueBlock(label = stringResource(id = localR.string.wildlife_label), value = entry.wildlifeSightings)
            }

            if (entry.resupplyNotes.isNotBlank()) {
                LabelValueBlock(label = stringResource(id = localR.string.resupply_water_label), value = entry.resupplyNotes)
            }

            if (entry.notes.isNotBlank()) {
                LabelValueBlock(label = stringResource(id = localR.string.notes_label), value = entry.notes)
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
