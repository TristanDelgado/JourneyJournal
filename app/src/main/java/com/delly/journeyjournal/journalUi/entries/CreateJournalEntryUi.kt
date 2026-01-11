package com.delly.journeyjournal.journalUi.entries

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.genericUi.CustomTextField
import com.delly.journeyjournal.genericUi.DatePickerButton
import com.delly.journeyjournal.viewmodels.CreateEntryViewModel
import com.delly.journeyjournal.viewmodels.CreateEntryViewModelFactory
import com.delly.journeyjournal.R as localR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJournalEntryUi(
    navigateBack: () -> Unit,
    repository: JournalRepository,
    journalId: Long,
    entryId: Long? = null,
) {
    val viewModel: CreateEntryViewModel = viewModel(
        factory = CreateEntryViewModelFactory(
            navigateBack = navigateBack,
            repository = repository,
            journalId = journalId,
            entryId = entryId
        )
    )
    val context = LocalContext.current

    // Start of UI
    Column(
        modifier = Modifier
            .padding(dimensionResource(id = localR.dimen.screen_edge_padding))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            text = if (entryId == null) stringResource(id = localR.string.new_entry_title) else "Edit Entry",
            //stringResource( //TODO id = localR.string.edit_entry_title ),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        // Collecting States
        val dayNumber = viewModel.dayNumber.collectAsState()
        val prevDayNumber = viewModel.previousDayNumber.collectAsState()
        val selectedDate by viewModel.selectedDate.collectAsState()

        val startLocation = viewModel.startLocation.collectAsState()
        val startMile = viewModel.startMileMarker.collectAsState()

        val endLocation = viewModel.endLocation.collectAsState()
        val endMile = viewModel.endMileMarker.collectAsState()

        val distanceHiked = viewModel.distanceHiked.collectAsState()

        val elevationStart = viewModel.elevationStart.collectAsState()
        val elevationEnd = viewModel.elevationEnd.collectAsState()
        val netElevation = viewModel.netElevationChange.collectAsState()

        val sleptInBed = viewModel.sleptInBed.collectAsState()
        val tookShower = viewModel.tookShower.collectAsState()

        val trailConditions = viewModel.trailConditions.collectAsState()
        val weather = viewModel.weather.collectAsState()
        val wildlife = viewModel.wildlifeSightings.collectAsState()
        val resupply = viewModel.resupplyNotes.collectAsState()

        val dayRating = viewModel.dayRating.collectAsState()
        val moodRating = viewModel.moodRating.collectAsState()
        val notes = viewModel.notes.collectAsState()

        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = localR.dimen.content_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = localR.dimen.content_padding))
        ) {

            // 1. Day # and Date (Required Fields)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Side: Day Number
                Column(modifier = Modifier.weight(0.4f)) {
                    CustomTextField(
                        value = dayNumber.value,
                        onValueChange = { viewModel.updateDayNumber(it) },
                        label = stringResource(id = localR.string.day_number),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (entryId == null) {
                        // Prompt user where day number is from
                        Text(
                            text = "Prev: ${prevDayNumber.value}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                // Right Side: Date Picker
                Box(
                    modifier = Modifier.weight(0.6f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    DatePickerButton(
                        selectedDate = selectedDate,
                        onDateSelected = { viewModel.updateSelectedDate(it) },
                        placeholderText = stringResource(id = localR.string.select_date)
                    )
                }
            }

            HorizontalDivider()

            // 2 & 3. Start Section
            LocationSectionCard(
                sectionTitle = "Start Point",
                locationValue = startLocation.value,
                onLocationChange = { viewModel.updateStartLocation(it) },
                mileValue = startMile.value,
                onMileChange = { viewModel.updateStartMileMarker(it) },
                showDerivedFromText = entryId == null
            )

            // 4 & 5. End Section
            LocationSectionCard(
                sectionTitle = "End Point",
                locationValue = endLocation.value,
                onLocationChange = { viewModel.updateEndLocation(it) },
                mileValue = endMile.value,
                onMileChange = { viewModel.updateEndMileMarker(it) }
            )

            HorizontalDivider()

            // 6. Total Distance
            CustomTextField(
                value = distanceHiked.value,
                onValueChange = { /* Read only */ },
                label = stringResource(id = localR.string.distance_hiked),
                enabled = false
            )

            // 7, 8 & 9. Elevation Section (Boxed)
            SectionCard(title = "Elevation Profile") {
                CustomTextField(
                    value = elevationStart.value,
                    onValueChange = { viewModel.updateElevationStart(it) },
                    label = "Start Elevation",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                if (entryId == null) {
                    // Prompt user where start elevation is from
                    Text(
                        text = "Previous Entry End Elevation",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                CustomTextField(
                    value = elevationEnd.value,
                    onValueChange = { viewModel.updateElevationEnd(it) },
                    label = "End Elevation",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(8.dp))
                CustomTextField(
                    value = netElevation.value,
                    onValueChange = { /* Read only */ },
                    label = "Net Elevation Change",
                    enabled = false
                )
            }

            // 10 & 11. Comforts Section (Boxed)
            SectionCard(title = "Comforts") {
                // Bed Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Slept in a bed?")
                    Checkbox(
                        checked = sleptInBed.value,
                        onCheckedChange = { viewModel.toggleSleptInBed(it) }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // Shower Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Took a shower?")
                    Checkbox(
                        checked = tookShower.value,
                        onCheckedChange = { viewModel.toggleTookShower(it) }
                    )
                }
            }

            // 12. Trail Conditions
            GenericDropdown(
                label = stringResource(id = localR.string.trail_conditions),
                options = listOf(
                    "Dry/Good",
                    "Muddy",
                    "Snow/Ice",
                    "Rocky/Technical"
                ),
                selectedOption = trailConditions.value,
                onOptionSelected = { viewModel.updateTrailConditions(it) }
            )

            // 13. Weather
            GenericDropdown(
                label = stringResource(localR.string.weather),
                options = listOf(
                    "Sunny",
                    "Rain",
                    "Snow",
                    "Overcast"
                ),
                selectedOption = weather.value,
                onOptionSelected = { viewModel.updateWeather(it) }
            )

            // 14. Wildlife
            CustomTextField(
                value = wildlife.value,
                onValueChange = { viewModel.updateWildlifeSightings(it) },
                label = stringResource(id = localR.string.wildlife_sightings)
            )

            // 15. Resupply
            CustomTextField(
                value = resupply.value,
                onValueChange = { viewModel.updateResupplyNotes(it) },
                label = stringResource(id = localR.string.resupply_notes)
            )

            // 16. Photos Button
            Button(
                onClick = { /* TODO: Implement Photo Picker */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = localR.string.add_photos))
            }

            // 17. Day Rating
            GenericDropdown(
                label = "Day Rating",
                options = listOf(
                    "Bad",
                    "Tough",
                    "Good",
                    "Great"
                ),
                selectedOption = dayRating.value,
                onOptionSelected = { viewModel.updateDayRating(it) }
            )

            // 18. Mood Rating
            GenericDropdown(
                label = "Mood",
                options = listOf(
                    "Low",
                    "Neutral",
                    "Happy",
                    "Energetic"
                ),
                selectedOption = moodRating.value,
                onOptionSelected = { viewModel.updateMoodRating(it) }
            )

            // 19. Notes
            CustomTextField(
                value = notes.value,
                onValueChange = { viewModel.updateNotes(it) },
                label = stringResource(id = localR.string.journal_notes),
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )

            // Save/Cancel
            Row {
                Button(onClick = { viewModel.cancelEntry() }) {
                    Text(stringResource(id = localR.string.cancel))
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.saveEntry(onInvalidInput = {
                            Toast.makeText(
                                context,
                                "Day # and Date are required!",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }
                ) {
                    Text(stringResource(id = localR.string.save))
                }
            }
        }
    }
}

/**
 * Generic Section Card for grouping items
 */
@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

/**
 * Helper Card to section apart Location Data
 */
@Composable
fun LocationSectionCard(
    sectionTitle: String,
    locationValue: String,
    onLocationChange: (String) -> Unit,
    mileValue: String,
    onMileChange: (String) -> Unit,
    showDerivedFromText: Boolean = false,
) {
    SectionCard(title = sectionTitle) {
        CustomTextField(
            value = locationValue,
            onValueChange = onLocationChange,
            label = stringResource(id = localR.string.start_location).replace(
                "Start",
                ""
            ).replace(
                "End",
                ""
            ).trim()
        )
        if (showDerivedFromText) {
            // Prompt user where start location is from
            Text(
                text = "Previous Entry End Point",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(
            value = mileValue,
            onValueChange = onMileChange,
            label = "Mile Marker",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        if (showDerivedFromText) {
            // Prompt user where start location is from
            Text(
                text = "Previous Entry End Point Mile Marker",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

/**
 * Reusable Dropdown Component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
