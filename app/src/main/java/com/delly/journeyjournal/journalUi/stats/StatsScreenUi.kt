package com.delly.journeyjournal.journalUi.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.viewmodels.StatsViewModelFactory

/**
 * The main screen composable for displaying statistical data about a specific journey.
 *
 * This screen initializes the [StatsScreenViewModel] via a factory and observes the
 * [StatsUiState]. It handles the loading state and displays a responsive grid of
 * statistics categorized by Distance, Elevation, Lifestyle, and Hygiene.
 *
 * @param repository The [JournalRepository] used to access journal data.
 * @param journalId The unique identifier of the journal to load statistics for.
 */
@Composable
fun StatsScreenUi(
    repository: JournalRepository,
    journalId: Long,
) {
    val viewModel: StatsScreenViewModel = viewModel(
        factory = StatsViewModelFactory(
            repository = repository,
            journalId = journalId
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header Section
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Journal Statistics",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            // --- Distance Section ---
            item(span = { GridItemSpan(maxLineSpan) }) { SectionHeader("Distance") }

            item {
                StatCard(
                    "Avg Miles/Day",
                    uiState.avgMilesPerDay,
                    Icons.AutoMirrored.Filled.DirectionsWalk
                )
            }
            item {
                StatCard(
                    "Avg (No Zeros)",
                    uiState.avgMilesNoZeros,
                    Icons.AutoMirrored.Filled.DirectionsWalk
                )
            }
            item {
                StatCard(
                    "Avg Miles/Week",
                    uiState.avgMilesPerWeek,
                    Icons.AutoMirrored.Filled.DirectionsWalk
                )
            }
            item {
                StatCard(
                    "Avg Week (No Zeros)",
                    uiState.avgMilesPerWeek,
                    Icons.AutoMirrored.Filled.DirectionsWalk
                )
            }
            item {
                StatCard(
                    "Personal Best (PB)",
                    uiState.highestMileageDay,
                    Icons.Default.Terrain
                )
            }

            // --- Elevation Section ---
            item(span = { GridItemSpan(maxLineSpan) }) { SectionHeader("Elevation") }

            item {
                StatCard(
                    "Net Ascent",
                    uiState.totalNetAscent,
                    Icons.Default.Landscape
                )
            }
            item {
                StatCard(
                    "Net Descent",
                    uiState.totalNetDescent,
                    Icons.Default.Landscape
                )
            }
            item {
                StatCard(
                    "Max Climb",
                    uiState.biggestAscentDay,
                    Icons.Default.Landscape
                )
            }
            item {
                StatCard(
                    "Max Descent",
                    uiState.biggestDescentDay,
                    Icons.Default.Landscape
                )
            }

            // --- Lifestyle / Zeros ---
            item(span = { GridItemSpan(maxLineSpan) }) { SectionHeader("Rest & Recovery") }

            item {
                StatCard(
                    "Total Zeros",
                    uiState.totalZeros.toString(),
                    Icons.Default.Bed
                )
            }
            item {
                StatCard(
                    "Days Since Zero",
                    uiState.daysSinceLastZero.toString(),
                    Icons.AutoMirrored.Filled.DirectionsWalk
                )
            }
            item {
                StatCard(
                    "Nights Afield",
                    uiState.daysOnGround.toString(),
                    Icons.Default.Terrain
                )
            }
            item {
                StatCard(
                    "Nights in Bed",
                    uiState.daysInBed.toString(),
                    Icons.Default.Bed
                )
            }

            // --- Hygiene ---
            item(span = { GridItemSpan(maxLineSpan) }) { SectionHeader("Hygiene") }

            item {
                StatCard(
                    "Total Showers",
                    uiState.totalShowers.toString(),
                    Icons.Default.WaterDrop
                )
            }
            item {
                StatCard(
                    "Days Since Shower",
                    uiState.daysSinceLastShower.toString(),
                    Icons.Default.WaterDrop
                )
            }
            item {
                if (uiState.maxDaysWithoutShower == 1) {
                    StatCard(
                        "Max Shower Gap",
                        "${uiState.maxDaysWithoutShower} day",
                        Icons.Default.WaterDrop
                    )
                } else {
                    StatCard(
                        "Max Shower Gap",
                        "${uiState.maxDaysWithoutShower} days",
                        Icons.Default.WaterDrop
                    )
                }

            }
        }
    }
}

// --- Helper Composables ---

/**
 * A helper composable that acts as a header for different sections within the stats grid.
 *
 * @param title The text string to display as the section header.
 */
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(
            top = 8.dp,
            bottom = 4.dp
        )
    )
}

/**
 * A stylized card component used to display a single statistic.
 *
 * It consists of an icon, the primary value (e.g., "12.5"), and a label (e.g., "Avg Miles/Day").
 *
 * @param label The descriptive name of the statistic.
 * @param value The formatted value string to display.
 * @param icon The [ImageVector] icon representing the statistic category.
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp) // Fixed height for uniformity
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}