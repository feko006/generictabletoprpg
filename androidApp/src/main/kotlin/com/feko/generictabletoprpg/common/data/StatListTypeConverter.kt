package com.feko.generictabletoprpg.common.data

import androidx.room.TypeConverter
import com.feko.generictabletoprpg.common.domain.model.Stat

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