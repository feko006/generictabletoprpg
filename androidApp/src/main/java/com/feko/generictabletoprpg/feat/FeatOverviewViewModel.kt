package com.feko.generictabletoprpg.feat

import com.feko.generictabletoprpg.common.OverviewViewModel

class FeatOverviewViewModel(
    private val getAllFeatsUseCase: GetAllFeatsUseCase
) : OverviewViewModel<Feat>() {
    override fun getAllItems(): List<Feat> = getAllFeatsUseCase.getAll()
}