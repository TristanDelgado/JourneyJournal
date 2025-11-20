package com.delly.journeyjournal.enums


/**
 * Enum class representing different transportation methods.
 *
 * @property stringValue The string representation of the transportation method.
 */
enum class TransportationMethods(val stringValue: String) {
    ON_FOOT(stringValue = "On foot"),

    BICYCLE(stringValue = "Bicycle"),

    CAR(stringValue = "Car"),

    ANIMAL(stringValue = "Animal")
}
