package com.feko.generictabletoprpg.condition

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class ConditionDetailsViewModel(
    private val getConditionById: IGetById<Condition>
) : DetailsViewModel<Condition>() {
    override fun getItemById(id: Long): Condition = getConditionById.getById(id)
}