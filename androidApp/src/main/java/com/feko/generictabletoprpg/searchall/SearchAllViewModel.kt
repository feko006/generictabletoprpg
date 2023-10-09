package com.feko.generictabletoprpg.searchall

import com.feko.generictabletoprpg.common.OverviewViewModel

class SearchAllViewModel(
    private val searchAllUseCase: SearchAllUseCase
) : OverviewViewModel<Any>(null) {
    override fun getAllItems(): List<Any> = searchAllUseCase.getAllItems()
}