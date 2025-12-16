package com.delly.journeyjournal.journalUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.CheckBox
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
 * Side menu ui used for navigation inside of a journal.
 *
 * @param title The title of the journal
 * @param navigateHome Navigates the user to the home screen to view all journals
 * @receiver
 */
@Composable
fun SideMenuUi(
    title: String,
    isComplete: Boolean,
    navigateHome: () -> Unit,
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
            Text(
                text = title,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            Text(
                stringResource(id = localR.string.home_menu),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navigateHome()
                    },
                style = MaterialTheme.typography.titleMedium
            )

            HorizontalDivider()

            Text(
                stringResource(id = localR.string.section_1),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            NavigationDrawerItem(
                label = { Text(stringResource(id = localR.string.item_1)) },
                selected = false,
                onClick = { /* Handle click */ }
            )
            NavigationDrawerItem(
                label = { Text(stringResource(id = localR.string.item_2)) },
                selected = false,
                onClick = { /* Handle click */ }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (isComplete == false) {
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
                        navigateHome()
                        invertCompleteStatus()
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

            NavigationDrawerItem(
                label = { Text(stringResource(id = localR.string.settings)) },
                selected = false,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null
                    )
                },
                badge = { Text(stringResource(id = localR.string.placeholder_badge)) }, // Placeholder
                onClick = { /* Handle click */ }
            )
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
 * Side menu preview
 */
@Preview
@Composable
fun SideMenuPreview() {
    JourneyJournalTheme {
        SideMenuUi(
            title = "Preview Journey",
            isComplete = false,
            navigateHome = { },
            invertCompleteStatus = { },
            closeDrawer = { },
        )
    }
}
