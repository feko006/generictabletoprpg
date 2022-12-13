package com.feko.generictabletoprpg.feat

import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.DetailsViewModel

class FeatDetailsViewModel(
    private val getFeatByIdUseCase: GetFeatByIdUseCase
) : DetailsViewModel<Feat>() {
    override fun getItemById(id: Long) = getFeatByIdUseCase.getById(id)
}