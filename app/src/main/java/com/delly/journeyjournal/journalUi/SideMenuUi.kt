package com.delly.journeyjournal.journalUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delly.journeyjournal.theme.JourneyJournalTheme
import com.delly.journeyjournal.R as localR

/**
 * A composable that renders the side navigation menu (drawer) for a specific journal.
 *
 * This drawer provides navigation options to return to the home screen, adjust journal settings,
 * or toggle the journal's completion status. It adapts its content based on whether the
 * journal is currently marked as complete or active.
 *
 * @param title The title of the current journal, displayed at the top of the menu.
 * @param isComplete A boolean flag indicating if the journal is currently marked as complete.
 * This determines whether the "Mark as Complete" or "Mark as Incomplete" option is shown.
 * @param navigateHome Callback triggered when the "Home" navigation item is clicked.
 * Typically navigates the user back to the main journal list.
 * @param markCompleteAndNavHome Callback specific to the "Mark as Complete" action.
 * This is usually intended to mark the journal as done and immediately
 * navigate the user back to the home screen.
 * @param invertCompleteStatus Callback specific to the "Mark as Incomplete" action.
 * This toggles the status back to active without necessarily navigating away.
 * @param closeDrawer Callback to close the navigation drawer, typically invoked after an action is selected.
 */
@Composable
fun SideMenuUi(
    title: String,
    isComplete: Boolean,
    navigateHome: () -> Unit,
    markCompleteAndNavHome: () -> Unit,
    invertCompleteStatus: () -> Unit,
    closeDrawer: () -> Unit,
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            // Display the Journal Title
            Text(
                text = title,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )

            HorizontalDivider()

            // Option: Navigate Home
            NavigationDrawerItem(
                label = { Text(stringResource(id = localR.string.navigate_home)) },
                selected = false,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.House,
                        contentDescription = null
                    )
                },
                onClick = { navigateHome() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Option: Toggle Complete Status
            // If incomplete, show "Mark as Complete" and navigate home.
            // If complete, show "Mark as Incomplete" and stay on page.
            if (!isComplete) {
                NavigationDrawerItem(
                    label = { Text(stringResource(id = localR.string.mark_as_complete)) },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.CheckBox,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        markCompleteAndNavHome()
                    }
                )
            } else {
                NavigationDrawerItem(
                    label = { Text(stringResource(id = localR.string.mark_as_incomplete)) },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.CheckBox,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        invertCompleteStatus()
                        closeDrawer()
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Option: Settings (Placeholder)
            NavigationDrawerItem(
                label = { Text(stringResource(id = localR.string.settings)) },
                selected = false,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null
                    )
                },
                badge = { Text(stringResource(id = localR.string.placeholder_badge)) },
                onClick = { /* Handle click */ }
            )

            // Option: Help & Feedback (Placeholder)
            NavigationDrawerItem(
                label = { Text(stringResource(id = localR.string.help_and_feedback)) },
                selected = false,
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Help,
                        contentDescription = null
                    )
                },
                onClick = { /* Handle click */ },
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

/**
 * Preview for the [SideMenuUi] composable.
 * Displays the menu in a "Preview Journey" context with the journal marked as active.
 */
@Preview
@Composable
fun SideMenuPreview() {
    JourneyJournalTheme {
        SideMenuUi(
            title = "Preview Journey",
            isComplete = false,
            navigateHome = { },
            markCompleteAndNavHome = { },
            invertCompleteStatus = { },
            closeDrawer = { },
        )
    }
}
