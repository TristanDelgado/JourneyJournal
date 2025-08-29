package com.delly.journeyjournal.db

import androidx.room.TypeConverter
import com.delly.journeyjournal.enums.TransportationMethods

class Converters {
    @TypeConverter
    fun fromTransportationMethods(value: TransportationMethods): String {
        return value.name // Store as the enum constant's name
    }

    @TypeConverter
    fun toTransportationMethods(value: String): TransportationMethods {
        return TransportationMethods.valueOf(value) // Convert back from String
    }
}