package com.delly.journeyjournal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.tooling.preview.Preview
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JourneyEntity
import com.delly.journeyjournal.enums.TransportationMethods
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme
import com.delly.journeyjournal.ui.theme.Shapes
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

/**
 * A box that displays basic overall information about a journey.
 */
@Composable
fun JourneyOverviewBox(
    journeyEntity: JourneyEntity,
    navigateToJourney: (String) -> Unit,
    repository: JournalRepository?
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
            .combinedClickable(
                onClick = { navigateToJourney(journeyEntity.journeyName) }
            )
    ) {
        Row() {
            // Title
            Text(journeyEntity.journeyName)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {  },
                content = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Journey",
                    )
                }
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        repository?.deleteJourneyByName(name = journeyEntity.journeyName)
                    }
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Journey",
                    )
                }
            )
            Button(
                onClick = {  },
                content = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Delete Journey",
                    )
                }
            )
        }

        Row {
            Text("Mi: 25)")
            Text("Entries: 3)")
        }
        Text("Avg diff/day: 3.5/5.0")
    }
}

@Composable
@Preview(showBackground = true)
fun JourneyOverviewBoxPreview() {
    val journeyEntity = JourneyEntity(
        journeyName = "Test",
        journeymanName = "Test",
        courseName = "Test",
        courseRegion = "Test",
        startDate = 101010,
        transportationMethod = TransportationMethods.ON_FOOT,
        description = "Test"
    )

    JourneyJournalTheme {
        JourneyOverviewBox(
            journeyEntity = journeyEntity,
            navigateToJourney = { null },
            repository = null
        )
    }
}