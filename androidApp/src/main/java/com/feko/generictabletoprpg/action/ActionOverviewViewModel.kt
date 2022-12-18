package com.feko.generictabletoprpg.action

import com.feko.generictabletoprpg.common.OverviewViewModel

class ActionOverviewViewModel(
    private val getAllActionsUseCase: GetAllActionsUseCase
) : OverviewViewModel<Action>() {
    override fun getAllItems(): List<Action> =
        getAllActionsUseCase.getAll()
}