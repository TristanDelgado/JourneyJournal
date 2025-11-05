package com.delly.journeyjournal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.delly.journeyjournal.ui.theme.Typography
import com.delly.journeyjournal.R as localR

@Composable
fun JourneyEntriesUi() {
    Column(modifier = Modifier.fillMaxHeight()) {
        // TODO: Add a button to add a new journey
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
                onClick = { },
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
                text = "Entries",
                style = Typography.titleMedium,
            )
        }
//        // Display a list of active journeys
//        LazyColumn() {
//            items(count = 10) { item ->
//                JourneyOverviewBox()
//            }
//        }
    }
}