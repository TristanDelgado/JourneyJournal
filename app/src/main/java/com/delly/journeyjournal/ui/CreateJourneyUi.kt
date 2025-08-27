package com.delly.journeyjournal.ui

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.enums.TransportationMethods
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme
import com.delly.journeyjournal.ui.theme.Shapes
import com.delly.journeyjournal.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.delly.journeyjournal.R as localR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJourneyUi(navController: NavController) {
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

        var journeyName by remember { mutableStateOf("") }
        var journeymanName by remember { mutableStateOf("") }
        var courseName by remember { mutableStateOf("") }
        var courseRegion by remember { mutableStateOf("") }
        var authorName by remember { mutableStateOf("") }
        var descriptionPurpose by remember { mutableStateOf("") }
        // Card-like container
        Column(
            modifier = Modifier
                .clip(Shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = localR.dimen.content_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = localR.dimen.content_padding))
        ) {
            CustomTextField(
                value = journeyName,
                onValueChange = { journeyName = it },
                label = stringResource(id = localR.string.journey_name)
            )

            CustomTextField(
                value = journeymanName,
                onValueChange = { journeymanName = it },
                label = "Journeyman Name"
            )

            CustomTextField(
                value = courseName,
                onValueChange = { courseName = it },
                label = "Course Name"
            )

            CustomTextField(
                value = courseRegion,
                onValueChange = { courseRegion = it },
                label = "Course Region"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DatePickerButton()
                Spacer(Modifier.weight(1f))
                TransportationMethodDropdownMenu()
            }

            CustomTextField(
                value = authorName,
                onValueChange = { authorName = it },
                label = "Author Name"
            )

            CustomTextField(
                value = descriptionPurpose,
                onValueChange = { descriptionPurpose = it },
                label = "Description / Purpose",
                singleLine = false
            )

            Row {
                Button(
                    onClick = { navController.navigate("home") }
                ) {
                    Text("Cancel")
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { navController.navigate("journeyView") }
                )
                {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton() {
    var showRangeModal by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    Button(onClick = { showRangeModal = true }) {
        Text(selectedDate?.let { date ->
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
                selectedDate = it
                showRangeModal = false
            },
            onDismiss = { showRangeModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportationMethodDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(value = TransportationMethods.ON_FOOT) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption.value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Choose one") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .widthIn(max = 150.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TransportationMethods.entries.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method.value) },
                    onClick = {
                        selectedOption = method
                        expanded = false
                    }
                )
            }
        }
    }
}

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

@Composable
@Preview(showBackground = true)
fun CreateJourneyUiPreview() {

    val mockNavController = rememberNavController()

    JourneyJournalTheme {
        CreateJourneyUi(navController = mockNavController)
    }
}