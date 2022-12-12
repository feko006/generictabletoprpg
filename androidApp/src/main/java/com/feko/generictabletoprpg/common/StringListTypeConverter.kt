package com.feko.generictabletoprpg.common

import androidx.room.TypeConverter

object StringListTypeConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String>): String =
        stringList.joinToString()

    @TypeConverter
    fun toStringList(string: String): List<String> =
        string.split(", ")
}