package com.feko.generictabletoprpg.condition

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class ConditionOverviewViewModel(
    private val getAllConditions: IGetAll<Condition>
) : OverviewViewModel<Condition>() {
    override fun getAllItems(): List<Condition> =
        getAllConditions.getAllSortedByName()
}