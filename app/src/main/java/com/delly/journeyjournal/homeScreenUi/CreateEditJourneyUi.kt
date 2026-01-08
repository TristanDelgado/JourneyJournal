package com.delly.journeyjournal.homeScreenUi

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.enums.DistanceUnit
import com.delly.journeyjournal.enums.TransportationMethods
import com.delly.journeyjournal.genericUi.CustomTextField
import com.delly.journeyjournal.genericUi.DatePickerButton
import com.delly.journeyjournal.viewmodels.CreateEditJournalViewModel
import com.delly.journeyjournal.viewmodels.CreateJournalViewModelFactory
import com.delly.journeyjournal.R as localR

/**
 * Create journal ui
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditJourneyUi(
    navigateHome: () -> Unit,
    navigateToJourney: (Long) -> Unit,
    repository: JournalRepository,
    journalToEditId: Long? = null,
) {
    // Initialize the viewmodel
    val viewModel: CreateEditJournalViewModel = viewModel(
        factory = CreateJournalViewModelFactory(
            navigateHome = navigateHome,
            navigateToJourney = navigateToJourney,
            repository = repository,
            journalToEditId = journalToEditId
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
            text = if (journalToEditId == null) {
                stringResource(id = localR.string.new_journal_title)
            } else {
                stringResource(id = localR.string.edit_journal_title)
            },
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Collecting States
        val journeyName = viewModel.journeyName.collectAsState()
        val journeymanName = viewModel.journeymanName.collectAsState()
        val courseName = viewModel.courseName.collectAsState()
        val courseRegion = viewModel.courseRegion.collectAsState()
        val descriptionPurpose = viewModel.description.collectAsState()
        val selectedDate by viewModel.selectedDate.collectAsState()

        // Wrapper Card
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = localR.dimen.content_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = localR.dimen.content_padding))
        ) {

            // 1. Identity Section
            SectionCard(title = "Identity") {
                // Journal Name
                CustomTextField(
                    value = journeyName.value,
                    onValueChange = { viewModel.updateJourneyName(it) },
                    label = stringResource(id = localR.string.journal_name)
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Journeyman Name
                CustomTextField(
                    value = journeymanName.value,
                    onValueChange = { viewModel.updateJourneymanName(it) },
                    label = stringResource(id = localR.string.journeyman_name)
                )
            }

            HorizontalDivider()

            // 2. Course Details
            SectionCard(title = "Course Details") {
                // Course Name
                CustomTextField(
                    value = courseName.value,
                    onValueChange = { viewModel.updateCourseName(it) },
                    label = stringResource(id = localR.string.course_name)
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Course Region
                CustomTextField(
                    value = courseRegion.value,
                    onValueChange = { viewModel.updateCourseRegion(it) },
                    label = stringResource(id = localR.string.course_region)
                )
            }

            HorizontalDivider()

            // 3. Logistics
            SectionCard(title = "Logistics") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Side: Date Picker
                    Box(modifier = Modifier.weight(0.5f)) {
                        DatePickerButton(
                            selectedDate = selectedDate,
                            onDateSelected = { viewModel.updateSelectedDate(it) }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Right Side: Transportation
                    Box(modifier = Modifier.weight(0.5f)) {
                        TransportationMethodDropdownMenu(viewModel = viewModel)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                DistanceUnitDropdownMenu(viewModel = viewModel)
            }

            // 4. Description/Notes
            CustomTextField(
                value = descriptionPurpose.value,
                onValueChange = { viewModel.updateDescription(it) },
                label = stringResource(id = localR.string.description_purpose),
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )

            // Save and Cancel buttons
            Row {
                Button(
                    onClick = { viewModel.cancelJourney() }
                ) {
                    Text(stringResource(id = localR.string.cancel))
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.saveJourney(onInvalidInput = {
                            Toast.makeText(
                                context,
                                "Journal Name is required!",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }
                )
                {
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
 * Transportation method dropdown menu
 * Styled to match GenericDropdown from entry UI
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
        OutlinedTextField(
            value = stringResource(id = selectedOption.value.labelResId),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = localR.string.travel_method)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TransportationMethods.entries.forEach { method ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = method.labelResId)) },
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
 * Distance unit dropdown menu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistanceUnitDropdownMenu(viewModel: CreateEditJournalViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = viewModel.selectedDistanceUnit.collectAsState()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = stringResource(id = selectedOption.value.labelResId),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = localR.string.distance_unit)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DistanceUnit.entries.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = unit.labelResId)) },
                    onClick = {
                        viewModel.updateDistanceUnit(distanceUnit = unit)
                        expanded = false
                    }
                )
            }
        }
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
