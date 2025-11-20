package com.delly.journeyjournal.db

import androidx.room.TypeConverter
import com.delly.journeyjournal.enums.TransportationMethods

/**
 * Type converters for the Room database.
 *
 * This class provides methods to convert custom types to and from types that Room can persist.
 * Specifically, it handles the conversion of [TransportationMethods] enum to String and vice versa.
 */
class Converters {
    /**
     * Converts a [TransportationMethods] enum value to its String representation.
     *
     * @param value The [TransportationMethods] enum to convert.
     * @return The name of the enum constant.
     */
    @TypeConverter
    fun fromTransportationMethods(value: TransportationMethods): String {
        return value.name
    }

    /**
     * Converts a String back to a [TransportationMethods] enum value.
     *
     * @param value The String representation of the enum.
     * @return The corresponding [TransportationMethods] enum constant.
     */
    @TypeConverter
    fun toTransportationMethods(value: String): TransportationMethods {
        return TransportationMethods.valueOf(value)
    }
}
