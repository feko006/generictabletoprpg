package com.feko.generictabletoprpg.action

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class ActionOverviewViewModel(
    private val getAll: IGetAll<Action>
) : OverviewViewModel<Action>() {
    override fun getAllItems(): List<Action> =
        getAll.getAllSortedByName()
}