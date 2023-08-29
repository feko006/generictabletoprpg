package com.feko.generictabletoprpg.action

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class ActionDetailsViewModel(
    private val getActionById: IGetById<Action>
) : DetailsViewModel<Action>() {
    override fun getItemById(id: Long): Action = getActionById.getById(id)
}