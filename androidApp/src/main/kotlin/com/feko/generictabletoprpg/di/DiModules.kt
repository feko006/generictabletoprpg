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
import com.feko.generictabletoprpg.common.UserPreferences
import com.feko.generictabletoprpg.condition.ConditionDao
import com.feko.generictabletoprpg.condition.ConditionDetailsViewModel
import com.feko.generictabletoprpg.disease.DiseaseDao
import com.feko.generictabletoprpg.disease.DiseaseDetailsViewModel
import com.feko.generictabletoprpg.feat.FeatDao
import com.feko.generictabletoprpg.feat.FeatDetailsViewModel
import com.feko.generictabletoprpg.import.IImportAllUseCase
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.import.IJsonImportAllUseCase
import com.feko.generictabletoprpg.import.IOrcbrewImportAllUseCase
import com.feko.generictabletoprpg.import.IOrcbrewImportAmmunitionsUseCase
import com.feko.generictabletoprpg.import.IOrcbrewImportArmorsUseCase
import com.feko.generictabletoprpg.import.IOrcbrewImportFeatsUseCase
import com.feko.generictabletoprpg.import.IOrcbrewImportSpellsUseCase
import com.feko.generictabletoprpg.import.IOrcbrewImportWeaponsUseCase
import com.feko.generictabletoprpg.import.IParseEdnAsMap
import com.feko.generictabletoprpg.import.IProcessEdnMap
import com.feko.generictabletoprpg.import.ImportAllUseCase
import com.feko.generictabletoprpg.import.ImportViewModel
import com.feko.generictabletoprpg.import.JsonImportAllUseCase
import com.feko.generictabletoprpg.import.MoshiJson
import com.feko.generictabletoprpg.import.OrcbrewImportAllUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportAmmunitionsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportArmorsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportFeatsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportSpellsUseCase
import com.feko.generictabletoprpg.import.OrcbrewImportWeaponsUseCase
import com.feko.generictabletoprpg.import.ParseEdnAsMapEdnJava
import com.feko.generictabletoprpg.import.ProcessEdnMapEdnJava
import com.feko.generictabletoprpg.init.ILoadBaseContent
import com.feko.generictabletoprpg.init.ILoadBaseContentUseCase
import com.feko.generictabletoprpg.init.LoadBaseContentAdapter
import com.feko.generictabletoprpg.init.LoadBaseContentUseCase
import com.feko.generictabletoprpg.room.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.searchall.ISearchAllUseCase
import com.feko.generictabletoprpg.searchall.SearchAllUseCase
import com.feko.generictabletoprpg.searchall.SearchAllViewModel
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.spell.SpellDetailsViewModel
import com.feko.generictabletoprpg.tracker.TrackedThingDao
import com.feko.generictabletoprpg.tracker.TrackedThingGroupDao
import com.feko.generictabletoprpg.tracker.TrackerGroupExportViewModelExtension
import com.feko.generictabletoprpg.tracker.TrackerGroupViewModel
import com.feko.generictabletoprpg.tracker.TrackerViewModel
import com.feko.generictabletoprpg.weapon.WeaponDao
import com.feko.generictabletoprpg.weapon.WeaponDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    // Services
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
    single<ILoadBaseContent> { LoadBaseContentAdapter(get()) }
    single<IJson> { MoshiJson() }

    // Use-cases
    single<ILoadBaseContentUseCase> { LoadBaseContentUseCase(get(), get(), get(), get()) }

    // VMs
    viewModel { AppViewModel(get()) }
}

val spellModule = module {
    single { get<GenericTabletopRpgDatabase>().spellDao() }

    viewModel { SpellDetailsViewModel(get<SpellDao>()) }
}

val featModule = module {
    single { get<GenericTabletopRpgDatabase>().featDao() }

    viewModel { FeatDetailsViewModel(get<FeatDao>()) }
}

val actionModule = module {
    single { get<GenericTabletopRpgDatabase>().actionDao() }

    viewModel { ActionDetailsViewModel(get<ActionDao>()) }
}

val conditionModule = module {
    single { get<GenericTabletopRpgDatabase>().conditionDao() }

    viewModel { ConditionDetailsViewModel(get<ConditionDao>()) }
}

val diseaseModule = module {
    single { get<GenericTabletopRpgDatabase>().diseaseDao() }

    viewModel { DiseaseDetailsViewModel(get<DiseaseDao>()) }
}

val weaponModule = module {
    single { get<GenericTabletopRpgDatabase>().weaponDao() }

    viewModel { WeaponDetailsViewModel(get<WeaponDao>()) }
}

val ammunitionModule = module {
    single { get<GenericTabletopRpgDatabase>().ammunitionDao() }

    viewModel { AmmunitionDetailsViewModel(get<AmmunitionDao>()) }
}

val armorModule = module {
    single { get<GenericTabletopRpgDatabase>().armorDao() }

    viewModel { ArmorDetailsViewModel(get<ArmorDao>()) }
}

val trackedThingGroupModule = module {
    single { get<GenericTabletopRpgDatabase>().trackedThingGroupDao() }
    single {
        TrackerGroupExportViewModelExtension(
            get<TrackedThingGroupDao>(),
            get<TrackedThingDao>(),
            get()
        )
    }
    viewModel { TrackerGroupViewModel(get(), get<TrackerGroupExportViewModelExtension>()) }
}

val trackedThingModule = module {
    single { get<GenericTabletopRpgDatabase>().trackedThingDao() }

    viewModel { params -> TrackerViewModel(params.get(), params.get(), get(), get()) }
}

val importModule = module {
    single<IOrcbrewImportSpellsUseCase> {
        OrcbrewImportSpellsUseCase(
            get(),
            get<SpellDao>()
        )
    }
    single<IOrcbrewImportFeatsUseCase> {
        OrcbrewImportFeatsUseCase(
            get(),
            get<FeatDao>()
        )
    }
    single<IOrcbrewImportWeaponsUseCase> {
        OrcbrewImportWeaponsUseCase(
            get(),
            get<WeaponDao>()
        )
    }
    single<IOrcbrewImportAmmunitionsUseCase> {
        OrcbrewImportAmmunitionsUseCase(
            get(),
            get<AmmunitionDao>()
        )
    }
    single<IOrcbrewImportArmorsUseCase> {
        OrcbrewImportArmorsUseCase(
            get(),
            get<ArmorDao>()
        )
    }
    single<IOrcbrewImportAllUseCase> {
        OrcbrewImportAllUseCase(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single<IJsonImportAllUseCase> {
        JsonImportAllUseCase(
            get(),
            get<ActionDao>(),
            get<ConditionDao>(),
            get<DiseaseDao>(),
            get<TrackedThingGroupDao>(),
            get<TrackedThingDao>()
        )
    }
    single<IImportAllUseCase> { ImportAllUseCase(get(), get()) }

    viewModel { ImportViewModel(get()) }
}

val searchAllModule = module {
    single<ISearchAllUseCase> {
        SearchAllUseCase(
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
