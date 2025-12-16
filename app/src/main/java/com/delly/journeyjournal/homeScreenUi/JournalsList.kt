package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.db.entities.JournalEntity

@Composable
fun JournalsList(
    journeys: List<JournalEntity>,
    repository: JournalRepository,
    navigateToJourney: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        header?.invoke()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(journeys) { journey ->
                JourneyOverviewBox(
                    journalEntity = journey,
                    navigateToJourney = navigateToJourney,
                    repository = repository,
                    onEditClick = onEditClick
                )
            }
        }
    }
}
