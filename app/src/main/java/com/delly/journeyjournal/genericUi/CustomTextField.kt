package com.delly.journeyjournal.genericUi

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A custom composable for a text field with a label.
 *
 * @param value The input text to be shown in the text field.
 * @param onValueChange The callback that is triggered when the input service updates the text.
 * @param label The label to be displayed inside the text field.
 * @param modifier The modifier to be applied to the text field.
 * @param singleLine When set to true, this text field becomes a single horizontally scrolling text field instead of a vertically scrolling one.
 * @param keyboardOptions software keyboard options such as a number-pad or regular keyboard.
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        enabled = enabled
    )
}
