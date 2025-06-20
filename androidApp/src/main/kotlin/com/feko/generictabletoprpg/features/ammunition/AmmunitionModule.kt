package com.feko.generictabletoprpg.features.ammunition

import com.feko.generictabletoprpg.features.ammunition.ui.AmmunitionDetailsViewModel
import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ammunitionModule = module {
    single { get<GenericTabletopRpgDatabase>().ammunitionDao() }
    viewModel { AmmunitionDetailsViewModel(get<AmmunitionDao>()) }
}
