package com.feko.generictabletoprpg.condition

import com.feko.generictabletoprpg.common.OverviewViewModel

class ConditionOverviewViewModel(
    private val getAllConditionsUseCase: GetAllConditionsUseCase
) : OverviewViewModel<Condition>() {
    override fun getAllItems(): List<Condition> =
        getAllConditionsUseCase.getAll()
}