package com.feko.generictabletoprpg.common

import androidx.room.TypeConverter

object StatListTypeConverter {
    @TypeConverter
    fun fromStatList(statList: List<Stat>): String =
        statList.joinToString { it.name }

    @TypeConverter
    fun toStatList(string: String): List<Stat> =
        if (string.isBlank()) {
            listOf()
        } else {
            string
                .split(", ")
                .map { Stat.valueOf(it) }
        }
}