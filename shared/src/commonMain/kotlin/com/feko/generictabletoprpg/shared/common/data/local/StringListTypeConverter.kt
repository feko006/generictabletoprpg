package com.feko.generictabletoprpg.shared.common.data.local

import androidx.room.TypeConverter

object StringListTypeConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String>): String =
        stringList.joinToString()

    @TypeConverter
    fun toStringList(string: String): List<String> =
        if (string.isEmpty()) {
            listOf()
        } else {
            string.split(", ")
        }
}