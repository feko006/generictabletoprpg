package com.feko.generictabletoprpg.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.feko.generictabletoprpg.action.ActionDao
import com.feko.generictabletoprpg.action.ActionEntity
import com.feko.generictabletoprpg.common.StatListTypeConverter
import com.feko.generictabletoprpg.common.StringListTypeConverter
import com.feko.generictabletoprpg.condition.ConditionDao
import com.feko.generictabletoprpg.condition.ConditionEntity
import com.feko.generictabletoprpg.feat.FeatDao
import com.feko.generictabletoprpg.feat.FeatEntity
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellEntity

@TypeConverters(
    StringListTypeConverter::class,
    StatListTypeConverter::class
)
@Database(
    entities = [
        SpellEntity::class,
        FeatEntity::class,
        ActionEntity::class,
        ConditionEntity::class
    ],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ]
)
abstract class GenericTabletopRpgDatabase : RoomDatabase() {
    abstract fun spellDao(): SpellDao
    abstract fun featDao(): FeatDao
    abstract fun actionDao(): ActionDao
    abstract fun conditionDao(): ConditionDao
}