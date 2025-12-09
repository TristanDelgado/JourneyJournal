package com.delly.journeyjournal.journalUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.viewmodels.JournalEntriesViewModel
import com.delly.journeyjournal.viewmodels.JournalEntriesViewModelFactory
import com.delly.journeyjournal.R as localR

/**
 * This composable displays a list of journal entries.
 * It also includes a button to create a new entry.
 *
 * @param navigateToCreateEntry A lambda to navigate to the create entry screen.
 * @param repository The repository to fetch data.
 * @param journeyId The id of the journey to fetch entries for.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyEntriesUi(
    navigateToCreateEntry: () -> Unit,
    repository: JournalRepository,
    journeyId: Int,
) {
    // Initialize the viewmodel
    val viewModel: JournalEntriesViewModel = viewModel(
        factory = JournalEntriesViewModelFactory(
            repository,
            journeyId
        )
    )

    // Start of UI
    Column(
        modifier = Modifier
            .padding(dimensionResource(id = localR.dimen.screen_edge_padding))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = localR.dimen.padding_small),
                    bottom = dimensionResource(id = localR.dimen.padding_small)
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button to create a new entry
            Button(
                onClick = { navigateToCreateEntry() },
                modifier = Modifier
                    .height(dimensionResource(id = localR.dimen.button_height_mini))
                    .width(dimensionResource(id = localR.dimen.button_height_mini)),
                contentPadding = PaddingValues(dimensionResource(id = localR.dimen.button_internal_padding_zero))
            ) {
                Icon(
                    modifier = Modifier.size(dimensionResource(id = localR.dimen.button_height_mini)),
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = localR.string.add_journey)
                )
            }
            Text(
                stringResource(id = localR.string.entries),
                style = Typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        val journeyWithEntries = viewModel.journalWithEntries.collectAsState()

        // Display a list of entries
        LazyColumn(
            modifier = Modifier.heightIn(
                min = 100.dp,
                max = dimensionResource(id = localR.dimen.lazy_list_height)
            )
        ) {

            // Check if we have valid data AND the list is not empty
            if (!journeyWithEntries.value?.entries.isNullOrEmpty()) {
                items(items = journeyWithEntries.value!!.entries) { entry ->
                    JourneyEntryOverviewBox(entry = entry)
                }
            } else {
                // Fallback: Show example entry if data is loading (null) or empty
                item {
                    val exampleEntry = JournalEntryEntity(
                        id = 0,
                        ownerId = 0,
                        dayNumber = stringResource(id = localR.string.example_day_number),
                        startLocation = stringResource(id = localR.string.example_start_location),
                        endLocation = stringResource(id = localR.string.example_end_location),
                        distanceHiked = stringResource(id = localR.string.example_distance),
                        trailConditions = stringResource(id = localR.string.example_trail_conditions),
                        wildlifeSightings = stringResource(id = localR.string.none),
                        resupplyNotes = stringResource(id = localR.string.none),
                        notes = stringResource(id = localR.string.example_notes)
                    )

                    JourneyEntryOverviewBox(entry = exampleEntry)
                }
            }
        }
    }
}
