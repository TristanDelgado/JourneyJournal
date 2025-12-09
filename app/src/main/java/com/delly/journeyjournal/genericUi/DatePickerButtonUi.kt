package com.delly.journeyjournal.genericUi

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.delly.journeyjournal.R as localR

/**
 * Date picker button
 *
 * @param selectedDate The selected date in the form of a Long.
 * @param onDateSelected The callback that is triggered when a date is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButtonUi(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
) {
    var showRangeModal by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = stringResource(id = localR.string.date_format)

    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat(dateFormat, Locale.US).format(Date(timestamp))
    }

    Button(onClick = { showRangeModal = true }) {
        Text(
            text = formatDate(timestamp = selectedDate)
        )
    }

    if (showRangeModal) {
        DatePickerDialog(
            onDismissRequest = { showRangeModal = false },
            confirmButton = {
                TextButton(onClick = {
                    // The default date should be the current date
                    val dateToSave: Long = datePickerState.selectedDateMillis
                        ?: System.currentTimeMillis()

                    onDateSelected(dateToSave)
                }) {
                    Text(stringResource(id = localR.string.OK))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRangeModal = false }) { Text(stringResource(id = localR.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
