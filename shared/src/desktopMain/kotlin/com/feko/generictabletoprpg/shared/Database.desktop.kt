package com.feko.generictabletoprpg.shared

import androidx.room.Room
import androidx.room.RoomDatabase
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<GenericTabletopRpgDatabase> {
    val userHome = System.getProperty("user.home")
    val dbDirectory = File(userHome, ".gttrpg")
    dbDirectory.mkdirs()
    val dbFile = File(dbDirectory, databaseName)
    return Room.databaseBuilder<GenericTabletopRpgDatabase>(
        name = dbFile.absolutePath,
    )
}