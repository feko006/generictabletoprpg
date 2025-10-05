package com.feko.generictabletoprpg.shared

import androidx.room.Room
import androidx.room.RoomDatabase
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<GenericTabletopRpgDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), databaseName)
    return Room.databaseBuilder<GenericTabletopRpgDatabase>(
        name = dbFile.absolutePath,
    )
}
