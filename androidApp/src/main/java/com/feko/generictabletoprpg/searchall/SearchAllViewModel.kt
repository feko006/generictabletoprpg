package com.feko.generictabletoprpg.searchall

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.common.OverviewViewModel

class SearchAllViewModel(
    private val getAllDaos: List<IGetAll<*>>
) : OverviewViewModel<Any>() {
    override fun getAllItems(): List<Any> =
        getAllDaos
            .flatMap {
                it.getAllSortedByName().filterNotNull()
            }.sortedBy {
                (it as Named).name
            }
}