package com.delly.journeyjournal.enums

import androidx.annotation.StringRes
import com.delly.journeyjournal.R

/**
 * Enum class representing different transportation methods.
 *
 * @property labelResId The string resource ID of the transportation method.
 */
enum class TransportationMethods(@StringRes val labelResId: Int) {
    ON_FOOT(labelResId = R.string.transport_on_foot),

    BICYCLE(labelResId = R.string.transport_bicycle),

    CAR(labelResId = R.string.transport_car),

    ANIMAL(labelResId = R.string.transport_animal)
}
