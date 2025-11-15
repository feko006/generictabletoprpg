package com.feko.generictabletoprpg.shared.features.tracker

import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerGroupViewModel
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val trackerGroupsModule = module {
    single { get<GenericTabletopRpgDatabase>().trackedThingGroupDao() }
    viewModelOf(::TrackerGroupViewModel)
}

val trackerModule = module {
    single { get<GenericTabletopRpgDatabase>().trackedThingDao() }
    viewModel { params -> TrackerViewModel(params.get(), params.get(), get(), get(), get()) }
}