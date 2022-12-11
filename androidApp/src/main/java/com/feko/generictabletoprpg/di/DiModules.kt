package com.feko.generictabletoprpg.di

import androidx.room.Room
import com.feko.generictabletoprpg.common.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.common.TimberLogger
import com.feko.generictabletoprpg.import.*
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

    // Use-cases
    single<OrcbrewImportUseCase> { OrcbrewImportUseCaseImpl(get(), get(), get(), get()) }

    // VMs
    viewModel { ImportViewModel(get()) }
}