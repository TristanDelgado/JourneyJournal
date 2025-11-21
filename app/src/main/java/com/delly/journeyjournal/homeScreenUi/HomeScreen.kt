package com.delly.journeyjournal.homeScreenUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.delly.journeyjournal.db.JournalRepository
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.R as localR

/**
 * The main screen of the application. It displays a title, a button to create a new journey,
 * and two lists of journeys: active and complete.
 *
 * @param navToCreateJourneyScreen A lambda function to be invoked when the user clicks the button to create a new journey.
 * @param navigateToJourney A lambda function that takes a journey name as a string and navigates to that journey's screen.
 * @param repository The repository to fetch journey data from.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navToCreateJourneyScreen: () -> Unit,
    navigateToJourney: (String) -> Unit,
    repository: JournalRepository
) {
    val allJourneys = repository.getAllJournals().collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(dimensionResource(id = localR.dimen.screen_edge_padding))) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = localR.string.home_title),
                style = Typography.headlineLarge
            )
        }

        HorizontalDivider()

        // Add a button to add a new journey
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = localR.dimen.padding_small),
                    bottom = dimensionResource(id = localR.dimen.padding_small)
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navToCreateJourneyScreen() },
                modifier = Modifier
                    .height(dimensionResource(id = localR.dimen.button_height_mini))
                    .width(dimensionResource(id = localR.dimen.button_height_mini)),
                contentPadding = PaddingValues(dimensionResource(id = localR.dimen.button_internal_padding_zero))
            ) {
                Icon(
                    modifier = Modifier.size(dimensionResource(id = localR.dimen.button_height_mini)),
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = localR.string.add_journey)
                )
            }
            Text(
                stringResource(id = localR.string.active_journeys),
                style = Typography.titleMedium,
            )
        }
        // Display a list of active journeys
        LazyColumn(
            modifier = Modifier.heightIn(
                min = 100.dp,
                max = dimensionResource(id = localR.dimen.lazy_list_height)
            )
        ) {
            allJourneys?.let { list ->
                items(list.value) { journey ->
                    JourneyOverviewBox(
                        journalEntity = journey,
                        navigateToJourney = navigateToJourney,
                        repository = repository
                    )
                }
            }
        }

        // Display a list of complete journeys
        Text(stringResource(id = localR.string.complete_journeys))
        LazyColumn {
            allJourneys?.let { list ->
                items(list.value) { journey ->
                    JourneyOverviewBox(
                        journalEntity = journey,
                        navigateToJourney = navigateToJourney,
                        repository = repository
                    )
                }
            }
        }
    }
}

// TODO: Add the ability to pass in an empty repository
///**
// * A preview composable for the `HomeScreen`.
// */
//@Composable
//@Preview(showBackground = true)
//fun HomeScreenPreview() {
//    JourneyJournalTheme {
//        HomeScreen(
//            navToCreateJourneyScreen = { null },
//            navigateToJourney = { null },
//            repository = null
//        )
//    }
//}
