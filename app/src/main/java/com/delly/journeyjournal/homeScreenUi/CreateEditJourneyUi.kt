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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.delly.journeyjournal.genericUi.DatePickerButton
import com.delly.journeyjournal.theme.Shapes
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.viewmodels.CreateEditJournalViewModel
import com.delly.journeyjournal.viewmodels.CreateJournalViewModelFactory
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
    journalToEditId: Int? = null,
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
            text = stringResource(id = localR.string.new_journey_title),
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
        val selectedDate by viewModel.selectedDate.collectAsState()

        Column(
            modifier = Modifier
                .clip(Shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = localR.dimen.content_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = localR.dimen.content_padding))
        ) {
            // Journal Name
            CustomTextField(
                value = journeyName.value,
                onValueChange = { viewModel.updateJourneyName(newName = it) },
                label = stringResource(id = localR.string.journal_name)
            )

            // Journeyman Name
            CustomTextField(
                value = journeymanName.value,
                onValueChange = { viewModel.updateJourneymanName(newName = it) },
                label = stringResource(id = localR.string.journeyman_name)
            )

            // Course Name
            CustomTextField(
                value = courseName.value,
                onValueChange = { viewModel.updateCourseName(newName = it) },
                label = stringResource(id = localR.string.course_name)
            )

            // Course Region
            CustomTextField(
                value = courseRegion.value,
                onValueChange = { viewModel.updateCourseRegion(newRegion = it) },
                label = stringResource(id = localR.string.course_region)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerButton(
                    selectedDate = selectedDate,
                    onDateSelected = { newDate ->
                        viewModel.updateSelectedDate(newDate = newDate)
                    }
                )
                Spacer(Modifier.weight(weight = 1f))
                TransportationMethodDropdownMenu(viewModel = viewModel)
            }

            CustomTextField(
                value = descriptionPurpose.value,
                onValueChange = { viewModel.updateDescription(newDescription = it) },
                label = stringResource(id = localR.string.description_purpose),
                singleLine = false
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
                    onClick = { viewModel.saveJourney() }
                )
                {
                    Text(stringResource(id = localR.string.save))
                }
            }
        }
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
            value = stringResource(id = selectedOption.value.labelResId),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = localR.string.choose_one)) },
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
