package com.delly.journeyjournal.ui

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.ui.theme.Typography
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

/**
 * This composable displays a list of journey entries.
 * It also includes a button to create a new entry.
 *
 * @param navigateToCreateEntry A lambda to navigate to the create entry screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyEntriesUi(
    navigateToCreateEntry: () -> Unit,
    repository: JournalRepository,
    journeyName: String,
) {
    val coroutineScope = rememberCoroutineScope()
    val journeyEntries: JourneyEntry? = null

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
            )
        }

        // Display a list of entries
        LazyColumn(
            modifier = Modifier.heightIn(
                min = 100.dp,
                max = dimensionResource(id = localR.dimen.lazy_list_height)
            )
        ) {
            coroutineScope.launch {
                val entries = repository.getJourneyByName(name = journeyName)?.entries;

                entries?.let { entry ->
                    items(entry) { journey ->
                        JourneyOverviewBox(
                            journeyEntity = journey,
                            navigateToJourney = navigateToJourney,
                            repository = repository
                        )
                    }
                }
            }
        }
    }
}
