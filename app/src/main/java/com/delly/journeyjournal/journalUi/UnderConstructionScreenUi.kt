package com.delly.journeyjournal.journalUi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.delly.journeyjournal.theme.Typography
import com.delly.journeyjournal.R as localR

/**
 * Under construction screen used as a placeholder.
 */
@Composable
fun UnderConstructionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = localR.string.under_construction),
            style = Typography.headlineLarge
        )
    }
}
