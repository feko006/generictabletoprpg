package com.feko.generictabletoprpg.di

import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.common.TimberLogger
import com.feko.generictabletoprpg.import.*
import com.feko.generictabletoprpg.spells.Spell
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diModules = module {
    // Services
    single<Logger> { TimberLogger() }

    // Ports & Adapters
    single<ParseEdnAsMapPort> { ParseEdnAsMapEdnJavaAdapter() }
    single<ProcessEdnMapPort> { ProcessEdnMapEdnJavaAdapter() }
    single<SaveSpellsPort> {
        object : SaveSpellsPort {
            override fun invoke(p1: List<Spell>): Result<Boolean> {
                return Result.success(true)
            }
        }
    }

    // Use-cases
    single<OrcbrewImportUseCase> { OrcbrewImportUseCaseImpl(get(), get(), get(), get()) }

    // VMs
    viewModel { ImportViewModel(get()) }
}