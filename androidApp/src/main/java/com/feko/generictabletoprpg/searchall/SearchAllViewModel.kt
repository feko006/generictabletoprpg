package com.feko.generictabletoprpg.searchall

import com.feko.generictabletoprpg.common.GetAllUseCase
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.common.OverviewViewModel

class SearchAllViewModel(
    private val getAllUseCases: List<GetAllUseCase<*>>
) : OverviewViewModel<Any>() {
    override fun getAllItems(): List<Any> =
        getAllUseCases
            .flatMap {
                it.getAll().filterNotNull()
            }.sortedBy {
                (it as Named).name
            }
}