package com.feko.generictabletoprpg.common

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellEntity

@TypeConverters(
    StringListTypeConverter::class
)
@Database(
    entities = [
        SpellEntity::class
    ],
    version = 1
)
abstract class GenericTabletopRpgDatabase : RoomDatabase() {
    abstract fun spellDao(): SpellDao
}