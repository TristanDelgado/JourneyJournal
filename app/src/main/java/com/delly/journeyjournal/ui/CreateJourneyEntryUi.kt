package com.delly.journeyjournal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.ui.genericUi.DatePickerButtonUi
import com.delly.journeyjournal.ui.viewmodels.CreateEntryViewModel
import com.delly.journeyjournal.ui.viewmodels.CreateEntryViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.delly.journeyjournal.R as localR

// --- Placeholders for your existing code/resources ---

// TODO: Replace with your actual repository
//interface JournalRepository {
//    // Define methods for adding entries, etc.
//}

// TODO: Replace with your actual CustomTextField
//@Composable
//fun CustomTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    singleLine: Boolean = true,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
//) {
//    TextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(label) },
//        singleLine = singleLine,
//        modifier = Modifier.fillMaxWidth(),
//        keyboardOptions = keyboardOptions
//    )
//}

// TODO: Replace with your actual DatePickerButton
//@Composable
//fun DatePickerButton(viewModel: CreateEntryViewModel) {
//    Button(onClick = { /* TODO: Show Date Picker Dialog */ }) {
//        Text(viewModel.entryDate.collectAsState().value.ifEmpty { "Select Date" })
//    }
//}

// TODO: Create and implement this Weather Dropdown
@Composable
fun WeatherDropdownMenu(viewModel: CreateEntryViewModel) {
    // Placeholder - Implement a dropdown for weather options
    Button(onClick = { /* TODO: Show Weather Dropdown */ }) {
        Text(viewModel.weather.collectAsState().value.ifEmpty { "Weather" })
    }
}

// TODO: Create and implement this Rating Dropdown
@Composable
fun RatingDropdownMenu(viewModel: CreateEntryViewModel) {
    // Placeholder - Implement a dropdown for 1-5 rating
    Button(onClick = { /* TODO: Show Rating Dropdown */ }) {
        Text("Rating: ${viewModel.physicalMentalState.collectAsState().value.ifEmpty { "N/A" }}")
    }
}
// --- End of Placeholders ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEntryUi(
    navigateBack: () -> Unit,
    repository: JournalRepository,
    journeyId: String
) {
    //Initialize the viewmodel
    val viewModel: CreateEntryViewModel = viewModel(
        factory = CreateEntryViewModelFactory(
            navigateBack = navigateBack,
            repository = repository,
            journeyId = journeyId
        )
    )

    // Start of UI
    Column(
        modifier = Modifier
            .padding(dimensionResource(id = localR.dimen.screen_edge_padding))
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Make the form scrollable
    ) {
        // Title
        Text(
            text = stringResource(id = localR.string.new_entry_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Start of form
        val dayNumber = viewModel.dayNumber.collectAsState()
        val startLocation = viewModel.startLocation.collectAsState()
        val endLocation = viewModel.endLocation.collectAsState()
        val distanceHiked = viewModel.distanceHiked.collectAsState()
        val trailConditions = viewModel.trailConditions.collectAsState()
        val wildlifeSightings = viewModel.wildlifeSightings.collectAsState()
        val resupplyNotes = viewModel.resupplyNotes.collectAsState()
        val notes = viewModel.notes.collectAsState()

        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = localR.dimen.content_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = localR.dimen.content_padding))
        ) {

            // Date and Day Number
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DatePickerButtonUi(
                    selectedDate = viewModel.entryDate.collectAsState().value,
                    onDateChanged = { viewModel.updateEntryDate(newDate = it) }
                )
                Spacer(Modifier.weight(1f))
                CustomTextField(
                    value = dayNumber.value,
                    onValueChange = { viewModel.updateDayNumber(it) },
                    label = stringResource(id = localR.string.day_number),
                    modifier = Modifier.width(100.dp) // Give day# a smaller width
                )
            }

            // Location
            CustomTextField(
                value = startLocation.value,
                onValueChange = { viewModel.updateStartLocation(it) },
                label = stringResource(id = localR.string.start_location)
            )
            CustomTextField(
                value = endLocation.value,
                onValueChange = { viewModel.updateEndLocation(it) },
                label = stringResource(id = localR.string.end_location)
            )

            // Distance and Weather
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    value = distanceHiked.value,
                    onValueChange = { viewModel.updateDistanceHiked(it) },
                    label = stringResource(id = localR.string.distance_hiked),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                WeatherDropdownMenu(viewModel = viewModel)
            }

            // Conditions
            CustomTextField(
                value = trailConditions.value,
                onValueChange = { viewModel.updateTrailConditions(it) },
                label = stringResource(id = localR.string.trail_conditions)
            )

            // Sightings
            CustomTextField(
                value = wildlifeSightings.value,
                onValueChange = { viewModel.updateWildlifeSightings(it) },
                label = stringResource(id = localR.string.wildlife_sightings)
            )

            // Practical Notes
            CustomTextField(
                value = resupplyNotes.value,
                onValueChange = { viewModel.updateResupplyNotes(it) },
                label = stringResource(id = localR.string.resupply_notes)
            )

            // Rating and Photos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingDropdownMenu(viewModel = viewModel)
                Spacer(Modifier.weight(1f))
                Button(onClick = { /* TODO: Implement Photo Picker Logic */ }) {
                    Text(stringResource(id = localR.string.add_photos))
                }
            }


            // Main Notes
            CustomTextField(
                value = notes.value,
                onValueChange = { viewModel.updateNotes(it) },
                label = stringResource(id = localR.string.journal_notes),
                singleLine = false,
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false) // Allow notes to expand
            )

            // Save and Cancel buttons
            Row {
                Button(
                    onClick = { viewModel.cancelEntry() }
                ) {
                    Text(stringResource(id = localR.string.cancel))
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { viewModel.saveEntry() }
                )
                {
                    Text(stringResource(id = localR.string.save))
                }
            }
        }
    }
}
