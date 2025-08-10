package com.feko.generictabletoprpg.features.tracker

import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.features.tracker.ui.TrackerGroupExportSubViewModel
import com.feko.generictabletoprpg.features.tracker.ui.TrackerGroupViewModel
import com.feko.generictabletoprpg.features.tracker.ui.TrackerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val trackerGroupsModule = module {
    single { get<GenericTabletopRpgDatabase>().trackedThingGroupDao() }
    single {
        TrackerGroupExportSubViewModel(
            get<TrackedThingGroupDao>(),
            get<TrackedThingDao>()
        )
    }
    viewModel { TrackerGroupViewModel(get(), get<TrackerGroupExportSubViewModel>()) }
}

val trackerModule = module {
    single { get<GenericTabletopRpgDatabase>().trackedThingDao() }
    viewModel { params -> TrackerViewModel(params.get(), params.get(), get(), get(), get()) }
}
