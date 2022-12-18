package com.feko.generictabletoprpg.di

import androidx.room.Room
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.action.ActionDao
import com.feko.generictabletoprpg.action.InsertActionsPort
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.common.TimberLogger
import com.feko.generictabletoprpg.common.UserPreferencesAdapter
import com.feko.generictabletoprpg.common.UserPreferencesPort
import com.feko.generictabletoprpg.feat.*
import com.feko.generictabletoprpg.import.*
import com.feko.generictabletoprpg.init.LoadBaseContentAdapter
import com.feko.generictabletoprpg.init.LoadBaseContentPort
import com.feko.generictabletoprpg.init.LoadBaseContentUseCase
import com.feko.generictabletoprpg.init.LoadBaseContentUseCaseImpl
import com.feko.generictabletoprpg.room.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.spell.*
import org.koin.androidx.viewmodel.dsl.viewModel
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

    // Ports & Adapters
    single<ParseEdnAsMapPort> { ParseEdnAsMapEdnJavaAdapter() }
    single<ProcessEdnMapPort> { ProcessEdnMapEdnJavaAdapter() }
    single {
        val spellDao = get<GenericTabletopRpgDatabase>().spellDao()
        spellDao.logger = get()
        spellDao
    }
    single<InsertSpellsPort> { get<SpellDao>() }
    single<GetAllSpellsPort> { get<SpellDao>() }
    single<GetSpellByIdPort> { get<SpellDao>() }
    single {
        val featDao = get<GenericTabletopRpgDatabase>().featDao()
        featDao.logger = get()
        featDao
    }
    single<InsertFeatsPort> { get<FeatDao>() }
    single<GetAllFeatsPort> { get<FeatDao>() }
    single<GetFeatByIdPort> { get<FeatDao>() }
    single<UserPreferencesPort> { UserPreferencesAdapter(get()) }
    single<LoadBaseContentPort> { LoadBaseContentAdapter(get()) }
    single<JsonPort> { MoshiJsonAdapter() }
    single {
        val actionDao = get<GenericTabletopRpgDatabase>().actionDao()
        actionDao.logger = get()
        actionDao
    }
    single<InsertActionsPort> { get<ActionDao>() }

    // Use-cases
    single<OrcbrewImportSpellsUseCase> { OrcbrewImportSpellsUseCaseImpl(get(), get(), get()) }
    single<OrcbrewImportFeatsUseCase> { OrcbrewImportFeatsUseCaseImpl(get(), get(), get()) }
    single<OrcbrewImportAllUseCase> { OrcbrewImportAllUseCaseImpl(get(), get(), get(), get()) }
    single<LoadBaseContentUseCase> { LoadBaseContentUseCaseImpl(get(), get(), get()) }
    single<GetAllSpellsUseCase> { GetAllSpellsUseCaseImpl(get()) }
    single<GetSpellByIdUseCase> { GetSpellByIdUseCaseImpl(get()) }
    single<GetAllFeatsUseCase> { GetAllFeatsUseCaseImpl(get()) }
    single<GetFeatByIdUseCase> { GetFeatByIdUseCaseImpl(get()) }
    single<JsonImportAllUseCase> { JsonImportAllUseCaseImpl(get(), get(), get()) }
    single<ImportAllUseCase> { ImportAllUseCaseImpl(get(), get()) }

    // VMs
    viewModel { ImportViewModel(get()) }
    viewModel { AppViewModel(get()) }
    viewModel { SpellOverviewViewModel(get()) }
    viewModel { SpellDetailsViewModel(get()) }
    viewModel { FeatOverviewViewModel(get()) }
    viewModel { FeatDetailsViewModel(get()) }
}