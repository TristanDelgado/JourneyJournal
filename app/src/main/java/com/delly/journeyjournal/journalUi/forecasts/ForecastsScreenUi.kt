package com.delly.journeyjournal.journalUi.forecasts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.viewmodels.ForecastsViewModelFactory
import java.text.DecimalFormat

/**
 * The primary screen composable for the Forecasts feature.
 *
 * This screen displays a dashboard containing:
 * 1. A header with hiking statistics (Last Mile Marker, Average Miles/Day).
 * 2. A scrollable list of future landmarks/forecasts.
 * 3. A Floating Action Button (FAB) to add new forecasts.
 *
 * It manages the UI state for the "Add/Edit" dialog and the "Delete Confirmation" dialog,
 * acting as the orchestrator between the user interactions and the [ForecastsScreenViewModel].
 *
 * @param repository The data repository used to initialize the ViewModel.
 * @param journalId The unique identifier of the journal for which forecasts are being displayed.
 */
@Composable
fun ForecastsScreenUi(
    repository: JournalRepository,
    journalId: Long,
) {
    val viewModel: ForecastsScreenViewModel = viewModel(
        factory = ForecastsViewModelFactory(
            repository = repository,
            journalId = journalId,
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    // State to control the dialog visibility
    var showDialog by remember { mutableStateOf(false) }
    // State to track if we are Editing (not null) or Adding (null)
    var forecastToEdit by remember { mutableStateOf<Forecast?>(null) }
    // NEW: State to track which forecast is selected for deletion
    var forecastToDelete by remember { mutableStateOf<Forecast?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    forecastToEdit = null // Reset to "Add Mode"
                    showDialog = true
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Forecast"
                )
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp
                    ),
            ) {
                ForecastHeader(
                    lastMileMarker = uiState.lastMileMarker,
                    averageMilesPerDay = uiState.averageMilesPerDay
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(
                        bottom = paddingValues.calculateBottomPadding() + 84.dp
                    )
                ) {
                    items(uiState.forecasts) { forecast ->
                        ForecastBox(
                            forecast = forecast,
                            onEditClick = {
                                forecastToEdit = it // Set "Edit Mode"
                                showDialog = true
                            },
                            // UPDATED: Instead of deleting immediately, set the state to trigger the dialog
                            onDeleteClick = {
                                forecastToDelete = it
                            }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            ForecastDialog(
                forecastToEdit = forecastToEdit, // Pass the existing data if editing
                onDismiss = { showDialog = false },
                onConfirm = { name, mileMarker ->
                    if (forecastToEdit == null) {
                        // Add Mode
                        viewModel.addForecast(
                            name,
                            mileMarker
                        )
                    } else {
                        // Edit Mode
                        viewModel.updateForecast(
                            forecastToEdit!!.id,
                            name,
                            mileMarker
                        )
                    }
                    showDialog = false
                }
            )
        }

        // NEW: Logic for Delete Confirmation Dialog
        if (forecastToDelete != null) {
            DeleteConfirmationDialog(
                forecastName = forecastToDelete!!.name,
                onDismiss = { forecastToDelete = null }, // Close dialog without doing anything
                onConfirm = {
                    viewModel.deleteForecast(forecastToDelete!!.id) // Perform delete
                    forecastToDelete = null // Close dialog
                }
            )
        }
    }
}

/**
 * A summary card displayed at the top of the Forecasts screen.
 *
 * It provides the user with context for the calculations below by showing:
 * - The last recorded mile marker from their journal.
 * - Their calculated average miles per day.
 *
 * @param lastMileMarker The user's most recent location (nullable if no entries exist).
 * @param averageMilesPerDay The calculated hiking pace (nullable if insufficient data).
 */
@Composable
private fun ForecastHeader(
    lastMileMarker: Double?,
    averageMilesPerDay: Double?,
) {
    val decimalFormat = remember { DecimalFormat("0.0") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Last Mile Marker",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = lastMileMarker?.let { decimalFormat.format(it) } ?: "N/A",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Avg Miles/Day",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = averageMilesPerDay?.let { decimalFormat.format(it) } ?: "N/A",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
            }
        }
    }
}

/**
 * A reusable dialog for creating or updating a Forecast.
 *
 * This composable adapts its UI (title, button text, initial values) based on whether
 * a [forecastToEdit] object is provided.
 * - If [forecastToEdit] is null: Acts as an "Add Forecast" dialog.
 * - If [forecastToEdit] is provided: Acts as an "Edit Forecast" dialog, pre-filling the fields.
 *
 * @param forecastToEdit The existing forecast object if editing, or null if adding new.
 * @param onDismiss Callback invoked when the user cancels or clicks outside.
 * @param onConfirm Callback invoked when the user clicks save/add. Provides the Name and Mile Marker.
 */
@Composable
private fun ForecastDialog(
    forecastToEdit: Forecast?,
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit,
) {
    // Initialize state with existing values if editing, or empty strings if adding
    var name by remember { mutableStateOf(forecastToEdit?.name ?: "") }
    var mileMarker by remember { mutableStateOf(forecastToEdit?.mileMarker?.toString() ?: "") }

    val isEditMode = forecastToEdit != null
    val dialogTitle = if (isEditMode) "Edit Forecast" else "Add Forecast"
    val confirmButtonText = if (isEditMode) "Save" else "Add"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = dialogTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = mileMarker,
                    onValueChange = { mileMarker = it },
                    label = { Text("Mile Marker") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val mileMarkerDouble = mileMarker.toDoubleOrNull()
                    if (name.isNotBlank() && mileMarkerDouble != null) {
                        onConfirm(
                            name,
                            mileMarkerDouble
                        )
                    }
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * A specialized dialog to confirm destructive deletion actions.
 *
 * Displays a warning message with the name of the forecast being deleted.
 * The "Delete" button is styled with an error color to indicate the severity of the action.
 *
 * @param forecastName The display name of the item being deleted (for context in the message).
 * @param onDismiss Callback invoked when the user cancels the deletion.
 * @param onConfirm Callback invoked when the user confirms the deletion.
 */
@Composable
private fun DeleteConfirmationDialog(
    forecastName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Forecast") },
        text = { Text(text = "Are you sure you want to delete \"$forecastName\"? This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                // Make the button red to indicate a destructive action
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            // Use TextButton for the cancel action so it's less prominent
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}