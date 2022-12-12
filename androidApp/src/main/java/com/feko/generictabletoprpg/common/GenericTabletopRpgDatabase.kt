package com.feko.generictabletoprpg.common

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.feat.FeatDao
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.feat.FeatEntity
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellEntity

@TypeConverters(
    StringListTypeConverter::class,
    StatListTypeConverter::class
)
@Database(
    entities = [
        SpellEntity::class,
        FeatEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class GenericTabletopRpgDatabase : RoomDatabase() {
    abstract fun spellDao(): SpellDao
    abstract fun featDao(): FeatDao
}