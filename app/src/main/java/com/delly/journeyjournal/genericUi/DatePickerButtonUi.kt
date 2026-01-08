package com.delly.journeyjournal.genericUi

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.delly.journeyjournal.R as localR

/**
 * A reusable button that opens a DatePickerDialog to select a date.
 *
 * This component manages its own dialog visibility state and provides a formatted
 * button label. It can be initialized with an existing date.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = stringResource(id = localR.string.start_date),
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // This state is for the DatePicker within the dialog.
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate ?: System.currentTimeMillis()
    )

    // A one-time effect to ensure the dialog's date picker is synchronized
    // if the external selectedDate changes.
    LaunchedEffect(selectedDate) {
        selectedDate?.let {
            datePickerState.selectedDateMillis = it
        }
    }

    // This function formats the timestamp for the button's text.
    val dateFormat = stringResource(id = localR.string.date_format)

    // Helper to format the date in UTC to match the Picker's output
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat(
            dateFormat,
            Locale.US
        )
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(timestamp))
    }

    // The button that the user clicks to open the dialog.
    Button(
        onClick = { showDatePicker = true },
        modifier = modifier
    ) {
        Text(
            text = selectedDate?.let { formatDate(it) } ?: placeholderText
        )
    }

    // This block displays the DatePickerDialog when showDatePicker is true.
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Only trigger the callback if a date has been selected in the picker.
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it)
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(id = localR.string.OK))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(id = localR.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                headline = {
                    val headerText = datePickerState.selectedDateMillis?.let {
                        formatDate(it)
                    } ?: placeholderText

                    Text(
                        text = headerText,
                        // Using 'headlineMedium' since the default was too large
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        )
                    )
                }
            )
        }
    }
}
