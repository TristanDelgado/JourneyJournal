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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date(timestamp))
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRangeModal = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}