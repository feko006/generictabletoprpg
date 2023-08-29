package com.feko.generictabletoprpg.feat

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById


class FeatDetailsViewModel(
    private val getFeatById: IGetById<Feat>
) : DetailsViewModel<Feat>() {
    override fun getItemById(id: Long) = getFeatById.getById(id)
}