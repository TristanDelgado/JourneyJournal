package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntity
import com.delly.journeyjournal.enums.TransportationMethods
import com.delly.journeyjournal.theme.JourneyJournalTheme
import com.delly.journeyjournal.theme.Shapes
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

/**
 * A box that displays basic overall information about a journey.
 */
@Composable
fun JourneyOverviewBox(
    journalEntity: JournalEntity,
    navigateToJourney: (Int) -> Unit,
    repository: JournalRepository?,
    onEditClick: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(
                dimensionResource(id = localR.dimen.padding_tiny)
            )
            .clip(Shapes.large)
            .background(color = Color.LightGray)
            .padding(
                dimensionResource(id = localR.dimen.padding_small)
            )
            .fillMaxWidth()
            .clickable(
                onClick = { navigateToJourney(journalEntity.id) }
            )
    ) {
        Row() {
            // Title
            Text(journalEntity.journalName.ifBlank { stringResource(id = localR.string.untitled_journal) })
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { onEditClick(journalEntity.id) },
                content = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = localR.string.edit_journey),
                    )
                }
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        repository?.deleteJournal(journalEntity = journalEntity)
                    }
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = localR.string.delete_journey),
                    )
                }
            )
            Button(
                onClick = {  },
                content = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(id = localR.string.journey_settings),
                    )
                }
            )
        }

        Row {
            Text(stringResource(id = localR.string.miles_abbrev) + stringResource(id = localR.string.dummy_miles))
            Text(stringResource(id = localR.string.entries_label) + stringResource(id = localR.string.dummy_entries))
        }
        Text(stringResource(id = localR.string.avg_diff_per_day) + stringResource(id = localR.string.dummy_avg_diff))
    }
}

@Composable
@Preview(showBackground = true)
fun JourneyOverviewBoxPreview() {
    val journalEntity = JournalEntity(
        journalName = "Test",
        journeymanName = "Test",
        courseName = "Test",
        courseRegion = "Test",
        startDate = 101010,
        transportationMethod = TransportationMethods.ON_FOOT,
        description = "Test"
    )

    JourneyJournalTheme {
        JourneyOverviewBox(
            journalEntity = journalEntity,
            navigateToJourney = { },
            repository = null,
            onEditClick = { }
        )
    }
}
