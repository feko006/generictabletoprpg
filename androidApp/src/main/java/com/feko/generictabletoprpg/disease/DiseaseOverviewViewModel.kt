package com.feko.generictabletoprpg.disease

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class DiseaseOverviewViewModel(
    private val getAllDiseases: IGetAll<Disease>
) : OverviewViewModel<Disease>() {
    override fun getAllItems(): List<Disease> =
        getAllDiseases.getAllSortedByName()
}