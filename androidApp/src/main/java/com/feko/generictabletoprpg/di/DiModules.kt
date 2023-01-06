package com.feko.generictabletoprpg.di

import androidx.room.Room
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.action.*
import com.feko.generictabletoprpg.ammunition.*
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.common.TimberLogger
import com.feko.generictabletoprpg.common.UserPreferencesAdapter
import com.feko.generictabletoprpg.common.UserPreferencesPort
import com.feko.generictabletoprpg.condition.*
import com.feko.generictabletoprpg.disease.*
import com.feko.generictabletoprpg.feat.*
import com.feko.generictabletoprpg.import.*
import com.feko.generictabletoprpg.init.LoadBaseContentAdapter
import com.feko.generictabletoprpg.init.LoadBaseContentPort
import com.feko.generictabletoprpg.init.LoadBaseContentUseCase
import com.feko.generictabletoprpg.init.LoadBaseContentUseCaseImpl
import com.feko.generictabletoprpg.room.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.searchall.SearchAllViewModel
import com.feko.generictabletoprpg.spell.*
import com.feko.generictabletoprpg.weapon.*
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
    includeImportDependencies()

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
                get<GetAllWeaponsUseCase>()
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
    single<OrcbrewImportAllUseCase> {
        OrcbrewImportAllUseCaseImpl(
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
