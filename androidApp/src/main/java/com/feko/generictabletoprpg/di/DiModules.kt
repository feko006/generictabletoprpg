package com.feko.generictabletoprpg.di

import androidx.room.Room
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.common.*
import com.feko.generictabletoprpg.import.*
import com.feko.generictabletoprpg.init.LoadBaseContentAdapter
import com.feko.generictabletoprpg.init.LoadBaseContentPort
import com.feko.generictabletoprpg.init.LoadBaseContentUseCase
import com.feko.generictabletoprpg.init.LoadBaseContentUseCaseImpl
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
    single<SaveSpellsPort> {
        val spellDao = get<GenericTabletopRpgDatabase>().spellDao()
        spellDao.logger = get()
        spellDao
    }
    single<UserPreferencesPort> { UserPreferencesAdapter(get()) }
    single<LoadBaseContentPort> { LoadBaseContentAdapter(get()) }

    // Use-cases
    single<OrcbrewImportUseCase> { OrcbrewImportUseCaseImpl(get(), get(), get(), get()) }
    single<LoadBaseContentUseCase> { LoadBaseContentUseCaseImpl(get(), get(), get()) }

    // VMs
    viewModel { ImportViewModel(get()) }
    viewModel { AppViewModel(get()) }
}