package com.feko.generictabletoprpg.searchall

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.INamed

class SearchAllUseCase(
    private val getAllDaos: List<IGetAll<*>>
) : ISearchAllUseCase {
    override fun getAllItems(): List<Any> =
        getAllDaos
            .flatMap {
                it.getAllSortedByName().filterNotNull()
            }.sortedBy {
                (it as INamed).name
            }
}