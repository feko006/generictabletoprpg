package com.feko.generictabletoprpg.features.condition

import com.feko.generictabletoprpg.features.condition.ui.ConditionDetailsViewModel
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.condition.ConditionDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val conditionModule = module {
    single { get<GenericTabletopRpgDatabase>().conditionDao() }
    viewModel { ConditionDetailsViewModel(get<ConditionDao>()) }
}
