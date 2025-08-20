package com.delly.journeyjournal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme
import com.delly.journeyjournal.ui.theme.Shapes
import com.delly.journeyjournal.R as localR


/**
 * A box that displays basic overall information about a journey.
 */
@Composable
fun JourneyOverviewBox() {
    Column(
        modifier = Modifier
            .padding(
                dimensionResource(id = localR.dimen.padding_tiny)
            )
            .clip(Shapes.large)
            .background(color = androidx.compose.ui.graphics.Color.LightGray)
            .padding(
                dimensionResource(id = localR.dimen.padding_small)
            )
            .fillMaxWidth()
    ) {
        Text("Big Rock Trail)")
        Row {
            Text("Mi: 25)")
            Text("Entries: 3)")
        }
        Text("Avg diff/day: 3.5/5.0")
    }
}

@Composable
@Preview(showBackground = true)
fun JourneyOverviewBoxPreview() {
    JourneyJournalTheme {
        JourneyOverviewBox()
    }
}