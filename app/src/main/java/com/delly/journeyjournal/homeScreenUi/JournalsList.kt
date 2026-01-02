package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.delly.journeyjournal.db.entities.JournalWithEntries

@Composable
fun JournalsList(
    journals: List<JournalWithEntries>,
    navigateToJournal: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (JournalWithEntries) -> Unit,
    onSettingsClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    emptyState: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        header?.invoke()

        if (journals.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(journals) { journal ->
                    JournalOverviewBox(
                        journalWithEntries = journal,
                        navigateToJournal = navigateToJournal,
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick,
                        onSettingsClick = onSettingsClick
                    )
                }
            }
        } else {
            emptyState?.invoke()
        }
    }
}
