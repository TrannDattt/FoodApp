package com.example.foodapp.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {
    @TypeConverter
    fun fromAnyToString(value: Any?): String? {
        if (value == null) {
            return null
        }

        return value as String
    }

    @TypeConverter
    fun fromStringToAny(value: String?): Any? {
        if (value == null) {
            return null
        }

        return value
    }

}