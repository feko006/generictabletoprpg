package com.feko.generictabletoprpg.features.feat

import com.feko.generictabletoprpg.features.feat.ui.FeatDetailsViewModel
import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featModule = module {
    single { get<GenericTabletopRpgDatabase>().featDao() }
    viewModel { FeatDetailsViewModel(get<FeatDao>()) }
}
