package com.feko.generictabletoprpg.features.action

import com.feko.generictabletoprpg.features.action.ui.ActionDetailsViewModel
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.action.ActionDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val actionModule = module {
    single { get<GenericTabletopRpgDatabase>().actionDao() }
    viewModel { ActionDetailsViewModel(get<ActionDao>()) }
}
