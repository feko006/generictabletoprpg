package com.feko.generictabletoprpg.shared

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

const val databaseName = "generic-tabletop-rpg.db"

expect fun getDatabaseBuilder(): RoomDatabase.Builder<GenericTabletopRpgDatabase>

fun getRoomDatabase(): GenericTabletopRpgDatabase {
    return getDatabaseBuilder()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
