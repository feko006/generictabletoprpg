package com.feko.generictabletoprpg.searchall

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.Named

class SearchAllUseCaseImpl(
    private val getAllDaos: List<IGetAll<*>>
) : SearchAllUseCase {
    override fun getAllItems(): List<Any> =
        getAllDaos
            .flatMap {
                it.getAllSortedByName().filterNotNull()
            }.sortedBy {
                (it as Named).name
            }
}