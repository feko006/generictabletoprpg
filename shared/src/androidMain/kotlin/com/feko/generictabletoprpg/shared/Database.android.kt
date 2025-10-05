package com.feko.generictabletoprpg.shared

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<GenericTabletopRpgDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(databaseName)
    return Room.databaseBuilder<GenericTabletopRpgDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}