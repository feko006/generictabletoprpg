package com.feko.generictabletoprpg.shared.features.ammunition

import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.ammunition.ui.AmmunitionDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ammunitionModule = module {
    single { get<GenericTabletopRpgDatabase>().ammunitionDao() }
    viewModel { AmmunitionDetailsViewModel(get<AmmunitionDao>()) }
}