package com.feko.generictabletoprpg.di

import androidx.room.Room
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.action.ActionDao
import com.feko.generictabletoprpg.action.ActionDetailsViewModel
import com.feko.generictabletoprpg.action.ActionOverviewViewModel
import com.feko.generictabletoprpg.action.GetActionByIdPort
import com.feko.generictabletoprpg.action.GetActionByIdUseCase
import com.feko.generictabletoprpg.action.GetActionByIdUseCaseImpl
import com.feko.generictabletoprpg.action.GetAllActionsPort
import com.feko.generictabletoprpg.action.GetAllActionsUseCase
import com.feko.generictabletoprpg.action.GetAllActionsUseCaseImpl
import com.feko.generictabletoprpg.action.InsertActionsPort
import com.feko.generictabletoprpg.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.ammunition.AmmunitionDetailsViewModel
import com.feko.generictabletoprpg.ammunition.AmmunitionOverviewViewModel
import com.feko.generictabletoprpg.ammunition.GetAllAmmunitionsPort
import com.feko.generictabletoprpg.ammunition.GetAllAmmunitionsUseCase
import com.feko.generictabletoprpg.ammunition.GetAllAmmunitionsUseCaseImpl
import com.feko.generictabletoprpg.ammunition.GetAmmunitionByIdPort
import com.feko.generictabletoprpg.ammunition.GetAmmunitionByIdUseCase
import com.feko.generictabletoprpg.ammunition.GetAmmunitionByIdUseCaseImpl
import com.feko.generictabletoprpg.ammunition.InsertAmmunitionsPort
import com.feko.generictabletoprpg.armor.ArmorDao
import com.feko.generictabletoprpg.armor.ArmorDetailsViewModel
import com.feko.generictabletoprpg.armor.ArmorOverviewViewModel
import com.feko.generictabletoprpg.armor.GetAllArmorsPort
import com.feko.generictabletoprpg.armor.GetAllArmorsUseCase
import com.feko.generictabletoprpg.armor.GetAllArmorsUseCaseImpl
import com.feko.generictabletoprpg.armor.GetArmorByIdPort
import com.feko.generictabletoprpg.armor.GetArmorByIdUseCase
import com.feko.generictabletoprpg.armor.GetArmorByIdUseCaseImpl
import com.feko.generictabletoprpg.armor.InsertArmorsPort
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackedThingDao
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackedThingGroupDao
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackerGroupViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackerViewModel
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.common.TimberLogger
import com.feko.generictabletoprpg.common.UserPreferencesAdapter
import com.feko.generictabletoprpg.common.UserPreferencesPort
import com.feko.generictabletoprpg.condition.ConditionDao
import com.feko.generictabletoprpg.condition.ConditionDetailsViewModel
import com.feko.generictabletoprpg.condition.ConditionOverviewViewModel
import com.feko.generictabletoprpg.condition.GetAllConditionsPort
import com.feko.generictabletoprpg.condition.GetAllConditionsUseCase
import com.feko.generictabletoprpg.condition.GetAllConditionsUseCaseImpl
import com.feko.generictabletoprpg.condition.GetConditionByIdPort
import com.feko.generictabletoprpg.condition.GetConditionByIdUseCase
import com.feko.generictabletoprpg.condition.GetConditionByIdUseCaseImpl
import com.feko.generictabletoprpg.condition.InsertConditionsPort
import com.feko.generictabletoprpg.disease.DiseaseDao
import com.feko.generictabletoprpg.disease.DiseaseDetailsViewModel
import com.feko.generictabletoprpg.disease.DiseaseOverviewViewModel
import com.feko.generictabletoprpg.disease.GetAllDiseasesPort
import com.feko.generictabletoprpg.disease.GetAllDiseasesUseCase
import com.feko.generictabletoprpg.disease.GetAllDiseasesUseCaseImpl
import com.feko.generictabletoprpg.disease.GetDiseaseByIdPort
import com.feko.generictabletoprpg.disease.GetDiseaseByIdUseCase
import com.feko.generictabletoprpg.disease.GetDiseaseByIdUseCaseImpl
import com.feko.generictabletoprpg.disease.InsertDiseasesPort
import com.feko.generictabletoprpg.feat.FeatDao
import com.feko.generictabletoprpg.feat.FeatDetailsViewModel
import com.feko.generictabletoprpg.feat.FeatOverviewViewModel
import com.feko.generictabletoprpg.feat.GetAllFeatsPort
import com.feko.generictabletoprpg.feat.GetAllFeatsUseCase
import com.feko.generictabletoprpg.feat.GetAllFeatsUseCaseImpl
import com.feko.generictabletoprpg.feat.GetFeatByIdPort
import com.feko.generictabletoprpg.feat.GetFeatByIdUseCase
import com.feko.generictabletoprpg.feat.GetFeatByIdUseCaseImpl
import com.feko.generictabletoprpg.feat.InsertFeatsPort
import com.feko.generictabletoprpg.import.ImportAllUseCase
import com.feko.generictabletoprpg.import.ImportAllUseCaseImpl
import com.feko.generictabletoprpg.import.ImportViewModel
import com.feko.generictabletoprpg.import.JsonImportAllUseCase
import com.feko.generictabletoprpg.import.JsonImportAllUseCaseImpl
import com.feko.generictabletoprpg.import.JsonPort
import com.feko.generictabletoprpg.import.MoshiJsonAdapter
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
import com.feko.generictabletoprpg.import.ParseEdnAsMapEdnJavaAdapter
import com.feko.generictabletoprpg.import.ParseEdnAsMapPort
import com.feko.generictabletoprpg.import.ProcessEdnMapEdnJavaAdapter
import com.feko.generictabletoprpg.import.ProcessEdnMapPort
import com.feko.generictabletoprpg.init.LoadBaseContentAdapter
import com.feko.generictabletoprpg.init.LoadBaseContentPort
import com.feko.generictabletoprpg.init.LoadBaseContentUseCase
import com.feko.generictabletoprpg.init.LoadBaseContentUseCaseImpl
import com.feko.generictabletoprpg.room.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.searchall.SearchAllViewModel
import com.feko.generictabletoprpg.spell.GetAllSpellsPort
import com.feko.generictabletoprpg.spell.GetAllSpellsUseCase
import com.feko.generictabletoprpg.spell.GetAllSpellsUseCaseImpl
import com.feko.generictabletoprpg.spell.GetSpellByIdPort
import com.feko.generictabletoprpg.spell.GetSpellByIdUseCase
import com.feko.generictabletoprpg.spell.GetSpellByIdUseCaseImpl
import com.feko.generictabletoprpg.spell.InsertSpellsPort
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellDetailsViewModel
import com.feko.generictabletoprpg.spell.SpellOverviewViewModel
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingGroupPort
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingGroupUseCase
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingGroupUseCaseImpl
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingPort
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingUseCase
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingUseCaseImpl
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingGroupsPort
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingGroupsUseCase
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingGroupsUseCaseImpl
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingsByGroupPort
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingsUseCase
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingsUseCaseImpl
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingGroupPort
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingGroupUseCase
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingGroupUseCaseImpl
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingPort
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingUseCase
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingUseCaseImpl
import com.feko.generictabletoprpg.weapon.GetAllWeaponsPort
import com.feko.generictabletoprpg.weapon.GetAllWeaponsUseCase
import com.feko.generictabletoprpg.weapon.GetAllWeaponsUseCaseImpl
import com.feko.generictabletoprpg.weapon.GetWeaponByIdPort
import com.feko.generictabletoprpg.weapon.GetWeaponByIdUseCase
import com.feko.generictabletoprpg.weapon.GetWeaponByIdUseCaseImpl
import com.feko.generictabletoprpg.weapon.InsertWeaponsPort
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

    // Ports & Adapters
    single<ParseEdnAsMapPort> { ParseEdnAsMapEdnJavaAdapter() }
    single<ProcessEdnMapPort> { ProcessEdnMapEdnJavaAdapter() }
    single<UserPreferencesPort> { UserPreferencesAdapter(get()) }
    single<LoadBaseContentPort> { LoadBaseContentAdapter(get()) }
    single<JsonPort> { MoshiJsonAdapter() }

    // Use-cases
    single<LoadBaseContentUseCase> { LoadBaseContentUseCaseImpl(get(), get(), get(), get()) }

    // VMs
    viewModel { AppViewModel(get()) }
    viewModel {
        SearchAllViewModel(
            listOf(
                get<GetAllSpellsUseCase>(),
                get<GetAllFeatsUseCase>(),
                get<GetAllActionsUseCase>(),
                get<GetAllConditionsUseCase>(),
                get<GetAllDiseasesUseCase>(),
                get<GetAllWeaponsUseCase>(),
                get<GetAllAmmunitionsUseCase>(),
                get<GetAllArmorsUseCase>()
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
    single<InsertSpellsPort> { get<SpellDao>() }
    single<GetAllSpellsPort> { get<SpellDao>() }
    single<GetSpellByIdPort> { get<SpellDao>() }

    single<GetAllSpellsUseCase> { GetAllSpellsUseCaseImpl(get()) }
    single<GetSpellByIdUseCase> { GetSpellByIdUseCaseImpl(get()) }

    viewModel { SpellOverviewViewModel(get()) }
    viewModel { SpellDetailsViewModel(get()) }
}

fun Module.includeFeatDependencies() {
    single {
        val featDao = get<GenericTabletopRpgDatabase>().featDao()
        featDao.logger = get()
        featDao
    }
    single<InsertFeatsPort> { get<FeatDao>() }
    single<GetAllFeatsPort> { get<FeatDao>() }
    single<GetFeatByIdPort> { get<FeatDao>() }

    single<GetAllFeatsUseCase> { GetAllFeatsUseCaseImpl(get()) }
    single<GetFeatByIdUseCase> { GetFeatByIdUseCaseImpl(get()) }

    viewModel { FeatOverviewViewModel(get()) }
    viewModel { FeatDetailsViewModel(get()) }
}

fun Module.includeActionDependencies() {
    single {
        val actionDao = get<GenericTabletopRpgDatabase>().actionDao()
        actionDao.logger = get()
        actionDao
    }
    single<InsertActionsPort> { get<ActionDao>() }
    single<GetAllActionsPort> { get<ActionDao>() }
    single<GetActionByIdPort> { get<ActionDao>() }

    single<GetAllActionsUseCase> { GetAllActionsUseCaseImpl(get()) }
    single<GetActionByIdUseCase> { GetActionByIdUseCaseImpl(get()) }

    viewModel { ActionOverviewViewModel(get()) }
    viewModel { ActionDetailsViewModel(get()) }
}

fun Module.includeConditionDependencies() {
    single {
        val conditionDao = get<GenericTabletopRpgDatabase>().conditionDao()
        conditionDao.logger = get()
        conditionDao
    }
    single<InsertConditionsPort> { get<ConditionDao>() }
    single<GetAllConditionsPort> { get<ConditionDao>() }
    single<GetConditionByIdPort> { get<ConditionDao>() }

    single<GetAllConditionsUseCase> { GetAllConditionsUseCaseImpl(get()) }
    single<GetConditionByIdUseCase> { GetConditionByIdUseCaseImpl(get()) }

    viewModel { ConditionOverviewViewModel(get()) }
    viewModel { ConditionDetailsViewModel(get()) }
}

fun Module.includeDiseaseDependencies() {
    single {
        val diseaseDao = get<GenericTabletopRpgDatabase>().diseaseDao()
        diseaseDao.logger = get()
        diseaseDao
    }
    single<InsertDiseasesPort> { get<DiseaseDao>() }
    single<GetAllDiseasesPort> { get<DiseaseDao>() }
    single<GetDiseaseByIdPort> { get<DiseaseDao>() }

    single<GetAllDiseasesUseCase> { GetAllDiseasesUseCaseImpl(get()) }
    single<GetDiseaseByIdUseCase> { GetDiseaseByIdUseCaseImpl(get()) }

    viewModel { DiseaseOverviewViewModel(get()) }
    viewModel { DiseaseDetailsViewModel(get()) }
}

fun Module.includeWeaponDependencies() {
    single {
        val weaponDao = get<GenericTabletopRpgDatabase>().weaponDao()
        weaponDao.logger = get()
        weaponDao
    }
    single<InsertWeaponsPort> { get<WeaponDao>() }
    single<GetAllWeaponsPort> { get<WeaponDao>() }
    single<GetWeaponByIdPort> { get<WeaponDao>() }

    single<GetAllWeaponsUseCase> { GetAllWeaponsUseCaseImpl(get()) }
    single<GetWeaponByIdUseCase> { GetWeaponByIdUseCaseImpl(get()) }

    viewModel { WeaponOverviewViewModel(get()) }
    viewModel { WeaponDetailsViewModel(get()) }
}

fun Module.includeAmmunitionDependencies() {
    single {
        val ammunitionDao = get<GenericTabletopRpgDatabase>().ammunitionDao()
        ammunitionDao.logger = get()
        ammunitionDao
    }
    single<InsertAmmunitionsPort> { get<AmmunitionDao>() }
    single<GetAllAmmunitionsPort> { get<AmmunitionDao>() }
    single<GetAmmunitionByIdPort> { get<AmmunitionDao>() }

    single<GetAllAmmunitionsUseCase> { GetAllAmmunitionsUseCaseImpl(get()) }
    single<GetAmmunitionByIdUseCase> { GetAmmunitionByIdUseCaseImpl(get()) }

    viewModel { AmmunitionOverviewViewModel(get()) }
    viewModel { AmmunitionDetailsViewModel(get()) }
}

fun Module.includeArmorDependencies() {
    single {
        val armorDao = get<GenericTabletopRpgDatabase>().armorDao()
        armorDao.logger = get()
        armorDao
    }
    single<InsertArmorsPort> { get<ArmorDao>() }
    single<GetAllArmorsPort> { get<ArmorDao>() }
    single<GetArmorByIdPort> { get<ArmorDao>() }

    single<GetAllArmorsUseCase> { GetAllArmorsUseCaseImpl(get()) }
    single<GetArmorByIdUseCase> { GetArmorByIdUseCaseImpl(get()) }

    viewModel { ArmorOverviewViewModel(get()) }
    viewModel { ArmorDetailsViewModel(get()) }
}

fun Module.includeTrackedThingGroupDependencies() {
    single {
        val trackedThingGroupDao = get<GenericTabletopRpgDatabase>().trackedThingGroupDao()
        trackedThingGroupDao.logger = get()
        trackedThingGroupDao
    }
    single<GetAllTrackedThingGroupsPort> { get<TrackedThingGroupDao>() }
    single<InsertOrUpdateTrackedThingGroupPort> { get<TrackedThingGroupDao>() }
    single<DeleteTrackedThingGroupPort> { get<TrackedThingGroupDao>() }

    single<GetAllTrackedThingGroupsUseCase> { GetAllTrackedThingGroupsUseCaseImpl(get()) }
    single<InsertOrUpdateTrackedThingGroupUseCase> {
        InsertOrUpdateTrackedThingGroupUseCaseImpl(get())
    }

    single<DeleteTrackedThingGroupUseCase> {
        DeleteTrackedThingGroupUseCaseImpl(get())
    }

    viewModel { TrackerGroupViewModel(get(), get(), get()) }
}

fun Module.includeTrackedThingDependencies() {
    single {
        val trackedThingDao = get<GenericTabletopRpgDatabase>().trackedThingDao()
        trackedThingDao.logger = get()
        trackedThingDao
    }

    single<GetAllTrackedThingsByGroupPort> { get<TrackedThingDao>() }
    single<InsertOrUpdateTrackedThingPort> { get<TrackedThingDao>() }
    single<DeleteTrackedThingPort> { get<TrackedThingDao>() }

    single<GetAllTrackedThingsUseCase> { GetAllTrackedThingsUseCaseImpl(get()) }
    single<InsertOrUpdateTrackedThingUseCase> { InsertOrUpdateTrackedThingUseCaseImpl(get()) }
    single<DeleteTrackedThingUseCase> { DeleteTrackedThingUseCaseImpl(get()) }

    viewModel { params -> TrackerViewModel(params.get(), get(), get(), get()) }
}

fun Module.includeImportDependencies() {
    single<OrcbrewImportSpellsUseCase> { OrcbrewImportSpellsUseCaseImpl(get(), get(), get()) }
    single<OrcbrewImportFeatsUseCase> { OrcbrewImportFeatsUseCaseImpl(get(), get(), get()) }
    single<OrcbrewImportWeaponsUseCase> { OrcbrewImportWeaponsUseCaseImpl(get(), get(), get()) }
    single<OrcbrewImportAmmunitionsUseCase> {
        OrcbrewImportAmmunitionsUseCaseImpl(
            get(),
            get(),
            get()
        )
    }
    single<OrcbrewImportArmorsUseCase> { OrcbrewImportArmorsUseCaseImpl(get(), get(), get()) }
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
    single<JsonImportAllUseCase> { JsonImportAllUseCaseImpl(get(), get(), get(), get(), get()) }
    single<ImportAllUseCase> { ImportAllUseCaseImpl(get(), get()) }

    viewModel { ImportViewModel(get()) }
}
