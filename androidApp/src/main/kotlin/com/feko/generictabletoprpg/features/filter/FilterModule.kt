package com.feko.generictabletoprpg.features.filter

import com.feko.generictabletoprpg.features.filter.ui.SpellFilterViewModel
import com.feko.generictabletoprpg.features.spell.SpellDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {
    viewModel { SpellFilterViewModel(get<SpellDao>()) }
}
