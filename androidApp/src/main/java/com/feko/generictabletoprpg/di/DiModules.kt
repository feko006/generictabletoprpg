package com.feko.generictabletoprpg.di

import androidx.room.Room
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.action.ActionDao
import com.feko.generictabletoprpg.action.ActionDetailsViewModel
import com.feko.generictabletoprpg.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.ammunition.AmmunitionDetailsViewModel
import com.feko.generictabletoprpg.armor.ArmorDao
import com.feko.generictabletoprpg.armor.ArmorDetailsViewModel
import com.feko.generictabletoprpg.common.IUserPreferences
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.common.TimberLogger
import com.feko.generictabletoprpg.common.UserPreferences
import com.feko.generictabletoprpg.condition.ConditionDao
import com.feko.generictabletoprpg.condition.ConditionDetailsViewModel
import com.feko.generictabletoprpg.disease.DiseaseDao
import com.feko.generictabletoprpg.disease.DiseaseDetailsViewModel
import com.feko.generictabletoprpg.feat.FeatDao
import com.feko.generictabletoprpg.feat.FeatDetailsViewModel
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
import com.feko.generictabletoprpg.searchall.SearchAllUseCase
import com.feko.generictabletoprpg.searchall.SearchAllUseCaseImpl
import com.feko.generictabletoprpg.searchall.SearchAllViewModel
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellDetailsViewModel
import com.feko.generictabletoprpg.tracker.TrackerGroupViewModel
import com.feko.generictabletoprpg.tracker.TrackerViewModel
import com.feko.generictabletoprpg.weapon.WeaponDao
import com.feko.generictabletoprpg.weapon.WeaponDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
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

    single<IParseEdnAsMap> { ParseEdnAsMapEdnJava() }
    single<IProcessEdnMap> { ProcessEdnMapEdnJava() }
    single<IUserPreferences> { UserPreferences(get()) }
    single<LoadBaseContentPort> { LoadBaseContentAdapter(get()) }
    single<IJson> { MoshiJson() }

    // Use-cases
    single<LoadBaseContentUseCase> { LoadBaseContentUseCaseImpl(get(), get(), get(), get()) }

    // VMs
    viewModel { AppViewModel(get()) }
}

val spellModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .spellDao()
            .apply {
                logger = get()
            }
    }

    viewModel { SpellDetailsViewModel(get<SpellDao>()) }
}

val featModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .featDao()
            .apply {
                logger = get()
            }
    }

    viewModel { FeatDetailsViewModel(get<FeatDao>()) }
}

val actionModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .actionDao()
            .apply {
                logger = get()
            }
    }

    viewModel { ActionDetailsViewModel(get<ActionDao>()) }
}

val conditionModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .conditionDao()
            .apply {
                logger = get()
            }
    }

    viewModel { ConditionDetailsViewModel(get<ConditionDao>()) }
}

val diseaseModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .diseaseDao()
            .apply {
                logger = get()
            }
    }

    viewModel { DiseaseDetailsViewModel(get<DiseaseDao>()) }
}

val weaponModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .weaponDao()
            .apply {
                logger = get()
            }
    }

    viewModel { WeaponDetailsViewModel(get<WeaponDao>()) }
}

val ammunitionModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .ammunitionDao()
            .apply {
                logger = get()
            }
    }

    viewModel { AmmunitionDetailsViewModel(get<AmmunitionDao>()) }
}

val armorModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .armorDao()
            .apply {
                logger = get()
            }
    }

    viewModel { ArmorDetailsViewModel(get<ArmorDao>()) }
}

val trackedThingGroupModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .trackedThingGroupDao()
            .apply {
                logger = get()
            }
    }

    viewModel { TrackerGroupViewModel(get()) }
}

val trackedThingModule = module {
    single {
        get<GenericTabletopRpgDatabase>()
            .trackedThingDao()
            .apply {
                logger = get()
            }
    }

    viewModel { params -> TrackerViewModel(params.get(), get(), get()) }
}

val importModule = module {
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

val searchAllModule = module {
    single<SearchAllUseCase> {
        SearchAllUseCaseImpl(
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
    viewModel { SearchAllViewModel(get()) }
}

val diModules = listOf(
    commonModule,
    spellModule,
    featModule,
    actionModule,
    conditionModule,
    diseaseModule,
    weaponModule,
    ammunitionModule,
    armorModule,
    importModule,
    trackedThingGroupModule,
    trackedThingModule,
    searchAllModule
)
