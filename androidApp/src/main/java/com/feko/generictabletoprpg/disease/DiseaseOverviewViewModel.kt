package com.feko.generictabletoprpg.disease

import com.feko.generictabletoprpg.common.OverviewViewModel

class DiseaseOverviewViewModel(
    private val getAllDiseasesUseCase: GetAllDiseasesUseCase
) : OverviewViewModel<Disease>() {
    override fun getAllItems(): List<Disease> =
        getAllDiseasesUseCase.getAll()
}