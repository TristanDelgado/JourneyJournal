package com.delly.journeyjournal.ui.genericUi

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButtonUi(
    selectedDate: Long,
    onDateChanged: (Long) -> Unit
) {
    var showRangeModal by remember { mutableStateOf(false) }

    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date(timestamp))
    }

    Button(onClick = { showRangeModal = true }) {
        Text(
            text = selectedDate?.let { date ->
                formatDate(timestamp = date)
            } ?: "Select Date"
        )
    }

    if (showRangeModal) {
        DatePickerModalUi(
            onDateSelected = {
                onDateChanged(it)
                showRangeModal = false
            },
            onDismiss = { showRangeModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalUi(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
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
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}