package com.feko.generictabletoprpg.shared

import androidx.room.Room
import androidx.room.RoomDatabase
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase

actual fun getDatabaseBuilder(): RoomDatabase.Builder<GenericTabletopRpgDatabase> {
    val appContext = appContext.applicationContext
    val dbFile = appContext.getDatabasePath(databaseName)
    return Room.databaseBuilder<GenericTabletopRpgDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}