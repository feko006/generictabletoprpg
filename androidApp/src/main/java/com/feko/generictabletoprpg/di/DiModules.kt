package com.feko.generictabletoprpg.di

import androidx.room.Room
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.action.ActionDao
import com.feko.generictabletoprpg.action.ActionDetailsViewModel
import com.feko.generictabletoprpg.action.ActionOverviewViewModel
import com.feko.generictabletoprpg.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.ammunition.AmmunitionDetailsViewModel
import com.feko.generictabletoprpg.ammunition.AmmunitionOverviewViewModel
import com.feko.generictabletoprpg.armor.ArmorDao
import com.feko.generictabletoprpg.armor.ArmorDetailsViewModel
import com.feko.generictabletoprpg.armor.ArmorOverviewViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackerGroupViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackerViewModel
import com.feko.generictabletoprpg.common.IUserPreferences
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.common.TimberLogger
import com.feko.generictabletoprpg.common.UserPreferences
import com.feko.generictabletoprpg.condition.ConditionDao
import com.feko.generictabletoprpg.condition.ConditionDetailsViewModel
import com.feko.generictabletoprpg.condition.ConditionOverviewViewModel
import com.feko.generictabletoprpg.disease.DiseaseDao
import com.feko.generictabletoprpg.disease.DiseaseDetailsViewModel
import com.feko.generictabletoprpg.disease.DiseaseOverviewViewModel
import com.feko.generictabletoprpg.feat.FeatDao
import com.feko.generictabletoprpg.feat.FeatDetailsViewModel
import com.feko.generictabletoprpg.feat.FeatOverviewViewModel
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.import.IParseEdnAsMap
import com.feko.generictabletoprpg.import.IProcessEdnMap
import com.feko.generictabletoprpg.import.ImportAllUseCase
import com.feko.generictabletoprpg.import.ImportAllUseCaseImpl
import com.feko.generictabletoprpg.import.ImportViewModel
import com.feko.generictabletoprpg.import.JsonImportAllUseCase
import com.feko.generictabletoprpg.import.JsonImportAllUseCaseImpl
import com.feko.generictabletoprpg.import.MoshiJson
import com.feko.generictabletoprpg.import.OrcbrewImportAllUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportAllUseCaseImpl
import com.feko.generictabletoprpg.import.OrcbrewImportAmmunitionsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportAmmunitionsUseCaseImpl
import com.feko.generictabletoprpg.import.OrcbrewImportArmorsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportArmorsUseCaseImpl
import com.feko.generictabletoprpg.import.OrcbrewImportFeatsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportFeatsUseCaseImpl
import com.feko.generictabletoprpg.import.OrcbrewImportSpellsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportSpellsUseCaseImpl
import com.feko.generictabletoprpg.import.OrcbrewImportWeaponsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportWeaponsUseCaseImpl
import com.feko.generictabletoprpg.import.ParseEdnAsMapEdnJava
import com.feko.generictabletoprpg.import.ProcessEdnMapEdnJava
import com.feko.generictabletoprpg.init.LoadBaseContentAdapter
import com.feko.generictabletoprpg.init.LoadBaseContentPort
import com.feko.generictabletoprpg.init.LoadBaseContentUseCase
import com.feko.generictabletoprpg.init.LoadBaseContentUseCaseImpl
import com.feko.generictabletoprpg.room.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.searchall.SearchAllViewModel
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellDetailsViewModel
import com.feko.generictabletoprpg.spell.SpellOverviewViewModel
import com.feko.generictabletoprpg.weapon.WeaponDao
import com.feko.generictabletoprpg.weapon.WeaponDetailsViewModel
import com.feko.generictabletoprpg.weapon.WeaponOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val diModules = module {
    // Services
    single<Logger> { TimberLogger() }
    single {
        Room
            .databaseBuilder(
                get(),
                GenericTabletopRpgDatabase::class.java,
                "generic-tabletop-rpg.db"
            )
            .build()
    }

    includeSpellDependencies()
    includeFeatDependencies()
    includeActionDependencies()
    includeConditionDependencies()
    includeDiseaseDependencies()
    includeWeaponDependencies()
    includeAmmunitionDependencies()
    includeArmorDependencies()
    includeImportDependencies()
    includeTrackedThingGroupDependencies()
    includeTrackedThingDependencies()

    single<IParseEdnAsMap> { ParseEdnAsMapEdnJava() }
    single<IProcessEdnMap> { ProcessEdnMapEdnJava() }
    single<IUserPreferences> { UserPreferences(get()) }
    single<LoadBaseContentPort> { LoadBaseContentAdapter(get()) }
    single<IJson> { MoshiJson() }

    // Use-cases
    single<LoadBaseContentUseCase> { LoadBaseContentUseCaseImpl(get(), get(), get(), get()) }

    // VMs
    viewModel { AppViewModel(get()) }
    viewModel {
        SearchAllViewModel(
            listOf(
                get<ActionDao>(),
                get<AmmunitionDao>(),
                get<ArmorDao>(),
                get<ConditionDao>(),
                get<DiseaseDao>(),
                get<FeatDao>(),
                get<SpellDao>(),
                get<WeaponDao>(),
            )
        )
    }
}

fun Module.includeSpellDependencies() {
    single {
        val spellDao = get<GenericTabletopRpgDatabase>().spellDao()
        spellDao.logger = get()
        spellDao
    }

    viewModel { SpellOverviewViewModel(get<SpellDao>()) }
    viewModel { SpellDetailsViewModel(get<SpellDao>()) }
}

fun Module.includeFeatDependencies() {
    single {
        val featDao = get<GenericTabletopRpgDatabase>().featDao()
        featDao.logger = get()
        featDao
    }

    viewModel { FeatOverviewViewModel(get<FeatDao>()) }
    viewModel { FeatDetailsViewModel(get<FeatDao>()) }
}

fun Module.includeActionDependencies() {
    single {
        val actionDao = get<GenericTabletopRpgDatabase>().actionDao()
        actionDao.logger = get()
        actionDao
    }

    viewModel { ActionOverviewViewModel(get<ActionDao>()) }
    viewModel { ActionDetailsViewModel(get<ActionDao>()) }
}

fun Module.includeConditionDependencies() {
    single {
        val conditionDao = get<GenericTabletopRpgDatabase>().conditionDao()
        conditionDao.logger = get()
        conditionDao
    }

    viewModel { ConditionOverviewViewModel(get<ConditionDao>()) }
    viewModel { ConditionDetailsViewModel(get<ConditionDao>()) }
}

fun Module.includeDiseaseDependencies() {
    single {
        val diseaseDao = get<GenericTabletopRpgDatabase>().diseaseDao()
        diseaseDao.logger = get()
        diseaseDao
    }

    viewModel { DiseaseOverviewViewModel(get<DiseaseDao>()) }
    viewModel { DiseaseDetailsViewModel(get<DiseaseDao>()) }
}

fun Module.includeWeaponDependencies() {
    single {
        val weaponDao = get<GenericTabletopRpgDatabase>().weaponDao()
        weaponDao.logger = get()
        weaponDao
    }

    viewModel { WeaponOverviewViewModel(get<WeaponDao>()) }
    viewModel { WeaponDetailsViewModel(get<WeaponDao>()) }
}

fun Module.includeAmmunitionDependencies() {
    single {
        val ammunitionDao = get<GenericTabletopRpgDatabase>().ammunitionDao()
        ammunitionDao.logger = get()
        ammunitionDao
    }

    viewModel { AmmunitionOverviewViewModel(get<AmmunitionDao>()) }
    viewModel { AmmunitionDetailsViewModel(get<AmmunitionDao>()) }
}

fun Module.includeArmorDependencies() {
    single {
        val armorDao = get<GenericTabletopRpgDatabase>().armorDao()
        armorDao.logger = get()
        armorDao
    }

    viewModel { ArmorOverviewViewModel(get<ArmorDao>()) }
    viewModel { ArmorDetailsViewModel(get<ArmorDao>()) }
}

fun Module.includeTrackedThingGroupDependencies() {
    single {
        val trackedThingGroupDao = get<GenericTabletopRpgDatabase>().trackedThingGroupDao()
        trackedThingGroupDao.logger = get()
        trackedThingGroupDao
    }

    viewModel { TrackerGroupViewModel(get()) }
}

fun Module.includeTrackedThingDependencies() {
    single {
        val trackedThingDao = get<GenericTabletopRpgDatabase>().trackedThingDao()
        trackedThingDao.logger = get()
        trackedThingDao
    }

    viewModel { params -> TrackerViewModel(params.get(), get()) }
}

fun Module.includeImportDependencies() {
    single<OrcbrewImportSpellsUseCase> {
        OrcbrewImportSpellsUseCaseImpl(
            get(),
            get<SpellDao>(),
            get()
        )
    }
    single<OrcbrewImportFeatsUseCase> {
        OrcbrewImportFeatsUseCaseImpl(
            get(),
            get<FeatDao>(),
            get()
        )
    }
    single<OrcbrewImportWeaponsUseCase> {
        OrcbrewImportWeaponsUseCaseImpl(
            get(),
            get<WeaponDao>(),
            get()
        )
    }
    single<OrcbrewImportAmmunitionsUseCase> {
        OrcbrewImportAmmunitionsUseCaseImpl(
            get(),
            get<AmmunitionDao>(),
            get()
        )
    }
    single<OrcbrewImportArmorsUseCase> {
        OrcbrewImportArmorsUseCaseImpl(
            get(),
            get<ArmorDao>(),
            get()
        )
    }
    single<OrcbrewImportAllUseCase> {
        OrcbrewImportAllUseCaseImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single<JsonImportAllUseCase> {
        JsonImportAllUseCaseImpl(
            get(),
            get(),
            get<ActionDao>(),
            get<ConditionDao>(),
            get<DiseaseDao>()
        )
    }
    single<ImportAllUseCase> { ImportAllUseCaseImpl(get(), get()) }

    viewModel { ImportViewModel(get()) }
}
