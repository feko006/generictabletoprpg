package com.feko.generictabletoprpg.features.condition

import com.feko.generictabletoprpg.features.condition.ui.ConditionDetailsViewModel
import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val conditionModule = module {
    single { get<GenericTabletopRpgDatabase>().conditionDao() }
    viewModel { ConditionDetailsViewModel(get<ConditionDao>()) }
}
