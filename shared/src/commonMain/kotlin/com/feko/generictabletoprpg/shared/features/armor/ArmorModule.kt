package com.feko.generictabletoprpg.shared.features.armor

import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.armor.ui.ArmorDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val armorModule = module {
    single { get<GenericTabletopRpgDatabase>().armorDao() }
    viewModel { ArmorDetailsViewModel(get<ArmorDao>()) }
}