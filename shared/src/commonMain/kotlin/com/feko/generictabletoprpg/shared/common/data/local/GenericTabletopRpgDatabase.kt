package com.feko.generictabletoprpg.shared.common.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.feko.generictabletoprpg.shared.features.action.ActionDao
import com.feko.generictabletoprpg.shared.features.action.ActionEntity
import com.feko.generictabletoprpg.shared.features.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.shared.features.ammunition.AmmunitionEntity
import com.feko.generictabletoprpg.shared.features.armor.ArmorDao
import com.feko.generictabletoprpg.shared.features.armor.ArmorEntity
import com.feko.generictabletoprpg.shared.features.condition.ConditionDao
import com.feko.generictabletoprpg.shared.features.condition.ConditionEntity
import com.feko.generictabletoprpg.shared.features.disease.DiseaseDao
import com.feko.generictabletoprpg.shared.features.disease.DiseaseEntity
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryDao
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryEntity
import com.feko.generictabletoprpg.shared.features.feat.FeatDao
import com.feko.generictabletoprpg.shared.features.feat.FeatEntity
import com.feko.generictabletoprpg.shared.features.spell.SpellDao
import com.feko.generictabletoprpg.shared.features.spell.SpellEntity
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingDao
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingEntity
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingGroupDao
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingGroupEntity
import com.feko.generictabletoprpg.shared.features.weapon.WeaponDao
import com.feko.generictabletoprpg.shared.features.weapon.WeaponEntity

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
        TrackedThingGroupEntity::class,
        InitiativeEntryEntity::class
    ],
    version = 15,
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
        AutoMigration(from = 14, to = 15)
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
    abstract fun initiativeEntryDao(): InitiativeEntryDao
}