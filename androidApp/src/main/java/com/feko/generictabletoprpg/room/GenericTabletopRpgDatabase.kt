package com.feko.generictabletoprpg.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.feko.generictabletoprpg.action.ActionDao
import com.feko.generictabletoprpg.action.ActionEntity
import com.feko.generictabletoprpg.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.ammunition.AmmunitionEntity
import com.feko.generictabletoprpg.armor.ArmorDao
import com.feko.generictabletoprpg.armor.ArmorEntity
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackedThingDao
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackedThingEntity
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackedThingGroupDao
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackedThingGroupEntity
import com.feko.generictabletoprpg.common.StatListTypeConverter
import com.feko.generictabletoprpg.common.StringListTypeConverter
import com.feko.generictabletoprpg.condition.ConditionDao
import com.feko.generictabletoprpg.condition.ConditionEntity
import com.feko.generictabletoprpg.disease.DiseaseDao
import com.feko.generictabletoprpg.disease.DiseaseEntity
import com.feko.generictabletoprpg.feat.FeatDao
import com.feko.generictabletoprpg.feat.FeatEntity
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellEntity
import com.feko.generictabletoprpg.weapon.WeaponDao
import com.feko.generictabletoprpg.weapon.WeaponEntity

@TypeConverters(
    StringListTypeConverter::class,
    StatListTypeConverter::class
)
@Database(
    entities = [
        SpellEntity::class,
        FeatEntity::class,
        ActionEntity::class,
        ConditionEntity::class,
        DiseaseEntity::class,
        WeaponEntity::class,
        AmmunitionEntity::class,
        ArmorEntity::class,
        TrackedThingEntity::class,
        TrackedThingGroupEntity::class
    ],
    version = 14,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13),
        AutoMigration(from = 13, to = 14),
    ]
)
abstract class GenericTabletopRpgDatabase : RoomDatabase() {
    abstract fun spellDao(): SpellDao
    abstract fun featDao(): FeatDao
    abstract fun actionDao(): ActionDao
    abstract fun conditionDao(): ConditionDao
    abstract fun diseaseDao(): DiseaseDao
    abstract fun weaponDao(): WeaponDao
    abstract fun ammunitionDao(): AmmunitionDao
    abstract fun armorDao(): ArmorDao
    abstract fun trackedThingDao(): TrackedThingDao
    abstract fun trackedThingGroupDao(): TrackedThingGroupDao
}