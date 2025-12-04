package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.enums.TransportationMethods
import com.delly.journeyjournal.genericUi.CustomTextField
import com.delly.journeyjournal.theme.Shapes
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.viewmodels.CreateEditJournalViewModel
import com.delly.journeyjournal.viewmodels.CreateJournalViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.delly.journeyjournal.R as localR

/**
 * Create journey ui
 *
 * @param navigateHome
 * @param navigateToJourney
 * @param repository
 * @receiver
 * @receiver
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditJourneyUi(
    navigateHome: () -> Unit,
    navigateToJourney: (Int) -> Unit,
    repository: JournalRepository,
    journalToEditId: Int? = null
) {
    //Initialize the viewmodel
    val viewModel: CreateEditJournalViewModel = viewModel(
        factory = CreateJournalViewModelFactory(
            navigateHome = navigateHome,
            navigateToJourney = navigateToJourney,
            repository = repository,
            journalToEditId = journalToEditId
        )
    )

    // Start of UI
    Column(
        modifier = Modifier
            .padding(dimensionResource(id = localR.dimen.screen_edge_padding))
            .fillMaxSize()
    ) {
        // Title
        Text(
            text = "New Journey",
            style = Typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Start of form
        val journeyName = viewModel.journeyName.collectAsState()
        val journeymanName = viewModel.journeymanName.collectAsState()
        val courseName = viewModel.courseName.collectAsState()
        val courseRegion = viewModel.courseRegion.collectAsState()
        val descriptionPurpose = viewModel.description.collectAsState()

        Column(
            modifier = Modifier
                .clip(Shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = localR.dimen.content_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = localR.dimen.content_padding))
        ) {
            CustomTextField(
                value = journeyName.value,
                onValueChange = { viewModel.updateJourneyName(newName = it) },
                label = stringResource(id = localR.string.journey_name)
            )

            CustomTextField(
                value = journeymanName.value,
                onValueChange = { viewModel.updateJourneymanName(newName = it) },
                label = "Journeyman Name"
            )

            CustomTextField(
                value = courseName.value,
                onValueChange = { viewModel.updateCourseName(newName = it) },
                label = "Course Name"
            )

            CustomTextField(
                value = courseRegion.value,
                onValueChange = { viewModel.updateCourseRegion(newRegion = it) },
                label = "Course Region"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerButton(viewModel = viewModel)
                Spacer(Modifier.weight(weight = 1f))
                TransportationMethodDropdownMenu(viewModel = viewModel)
            }

            CustomTextField(
                value = descriptionPurpose.value,
                onValueChange = { viewModel.updateDescription(newDescription = it) },
                label = "Description / Purpose",
                singleLine = false
            )

            // Save and Cancel buttons
            Row {
                Button(
                    onClick = { viewModel.cancelJourney() }
                ) {
                    Text("Cancel")
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { viewModel.saveJourney() }
                )
                {
                    Text("Save")
                }
            }
        }
    }
}

/**
 * Date picker button
 *
 * @param viewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(viewModel: CreateEditJournalViewModel) {
    var showRangeModal by remember { mutableStateOf(false) }
    val selectedDate = viewModel.selectedDate.collectAsState()

    Button(onClick = { showRangeModal = true }) {
        Text(selectedDate.value?.let { date ->
            SimpleDateFormat(
                "MM/dd/yyyy",
                Locale.US
            ).format(Date(date))
        }
            ?: "Start Date")
    }

    if (showRangeModal) {
        DatePickerModal(
            onDateSelected = {
                viewModel.updateSelectedDate(newDate = it)
                showRangeModal = false
            },
            onDismiss = { showRangeModal = false }
        )
    }
}

/**
 * Transportation method dropdown menu
 *
 * @param viewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportationMethodDropdownMenu(viewModel: CreateEditJournalViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = viewModel.selectedTransportationMethod.collectAsState()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption.value.stringValue,
            onValueChange = {},
            readOnly = true,
            label = { Text("Choose one") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .widthIn(max = 150.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TransportationMethods.entries.forEach { method ->
                DropdownMenuItem(
                    text = { Text(text = method.stringValue) },
                    onClick = {
                        viewModel.updateTransportationMethod(transportationMethod = method)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Date picker modal
 *
 * @param onDateSelected
 * @param onDismiss
 * @receiver
 * @receiver
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

//@Composable
//@Preview(showBackground = true)
//fun CreateJourneyUiPreview() {
//
//
//    JourneyJournalTheme {
//        CreateJourneyUi(
//            navController = mockNavController,
//            repository = null
//        )
//    }
//}
