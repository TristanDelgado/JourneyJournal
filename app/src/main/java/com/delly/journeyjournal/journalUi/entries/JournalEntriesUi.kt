package com.delly.journeyjournal.journalUi.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntryEntity
import com.delly.journeyjournal.enums.DistanceUnit
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.viewmodels.JournalEntriesViewModel
import com.delly.journeyjournal.viewmodels.JournalEntriesViewModelFactory
import com.delly.journeyjournal.R as localR

/**
 * This composable displays a list of journal entries.
 *
 * @param navigateToCreateEntry A lambda to navigate to the create entry screen.
 * @param repository The repository to fetch data.
 * @param journeyId The id of the journey to fetch entries for.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyEntriesUi(
    navigateToCreateEntry: (Long?) -> Unit,
    repository: JournalRepository,
    journeyId: Long,
) {
    // Initialize the viewmodel
    val viewModel: JournalEntriesViewModel = viewModel(
        factory = JournalEntriesViewModelFactory(
            repository,
            journeyId
        )
    )

    val journeyWithEntries by viewModel.journalWithEntries.collectAsState()
    val distanceUnit = journeyWithEntries?.journal?.distanceUnit ?: DistanceUnit.MILES
    var entryToDelete by remember { mutableStateOf<JournalEntryEntity?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToCreateEntry(null) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = localR.string.add_journal)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(), // Top Bar height + optional extra space
                bottom = paddingValues.calculateBottomPadding() + 80.dp, // Bottom Bar height + Space for FAB
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- 1. Header Section (Scrollable) ---
            item {
                Text(
                    text = stringResource(id = localR.string.entries),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // --- 2. List Content ---
            val entries = journeyWithEntries?.entries

            if (!entries.isNullOrEmpty()) {
                // Show actual entries (Reversed to show newest first)
                items(items = entries.reversed()) { entry ->
                    JournalEntryOverviewBox(
                        entry = entry,
                        distanceUnit = distanceUnit,
                        onEditClick = { navigateToCreateEntry(entry.id) },
                        onDeleteClick = { entryToDelete = it },
                    )
                }
            } else {
                // --- 3. Fallback / Example Content ---
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        text = stringResource(id = localR.string.exampleEntryBelow),
                        style = Typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

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
                        notes = stringResource(id = localR.string.example_notes),
                        date = System.currentTimeMillis(),
                        startMileMarker = "100.0",
                        endMileMarker = "115.5",
                        elevationStart = "1200",
                        elevationEnd = "2300",
                        netElevationChange = "1100",
                        sleptInBed = false,
                        tookShower = true,
                        weather = "Sunny",
                        dayRating = "5",
                        moodRating = "5"
                    )

                    JournalEntryOverviewBox(
                        entry = exampleEntry,
                        distanceUnit = distanceUnit,
                        onEditClick = { },
                        onDeleteClick = { },
                    )
                }
            }
        }
    }

    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = { Text(text = "Delete Entry") },
            text = { Text("Are you sure you want to delete this entry? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        entryToDelete?.let { viewModel.deleteEntry(it) }
                        entryToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { entryToDelete = null }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
