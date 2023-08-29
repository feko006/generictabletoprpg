package com.feko.generictabletoprpg.feat

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class FeatOverviewViewModel(
    private val getAllFeats: IGetAll<Feat>
) : OverviewViewModel<Feat>() {
    override fun getAllItems(): List<Feat> = getAllFeats.getAllSortedByName()
}