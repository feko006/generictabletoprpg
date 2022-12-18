package com.feko.generictabletoprpg.action

import com.feko.generictabletoprpg.common.DetailsViewModel

class ActionDetailsViewModel(
    private val getActionByIdUseCase: GetActionByIdUseCase
) : DetailsViewModel<Action>() {
    override fun getItemById(id: Long): Action = getActionByIdUseCase.getById(id)
}