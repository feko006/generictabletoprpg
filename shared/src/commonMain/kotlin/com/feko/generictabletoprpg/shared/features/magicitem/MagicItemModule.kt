package com.feko.generictabletoprpg.shared.features.magicitem

import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.magicitem.ui.MagicItemViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val magicItemModule = module {
    single { get<GenericTabletopRpgDatabase>().magicItemDao() }
    viewModel { MagicItemViewModel(get<MagicItemDao>()) }
}
