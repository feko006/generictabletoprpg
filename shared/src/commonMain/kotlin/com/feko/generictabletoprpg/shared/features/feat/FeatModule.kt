package com.feko.generictabletoprpg.shared.features.feat

import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.feat.ui.FeatDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featModule = module {
    single { get<GenericTabletopRpgDatabase>().featDao() }
    viewModel { FeatDetailsViewModel(get<FeatDao>()) }
}