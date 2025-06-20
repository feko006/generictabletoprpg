package com.feko.generictabletoprpg.features.armor

import com.feko.generictabletoprpg.features.armor.ui.ArmorDetailsViewModel
import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val armorModule = module {
    single { get<GenericTabletopRpgDatabase>().armorDao() }
    viewModel { ArmorDetailsViewModel(get<ArmorDao>()) }
}
