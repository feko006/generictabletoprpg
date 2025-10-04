package com.feko.generictabletoprpg.features.disease

import com.feko.generictabletoprpg.features.disease.ui.DiseaseDetailsViewModel
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.disease.DiseaseDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val diseaseModule = module {
    single { get<GenericTabletopRpgDatabase>().diseaseDao() }
    viewModel { DiseaseDetailsViewModel(get<DiseaseDao>()) }
}
