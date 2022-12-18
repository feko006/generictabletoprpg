package com.feko.generictabletoprpg.condition

import com.feko.generictabletoprpg.common.DetailsViewModel

class ConditionDetailsViewModel(
    private val getConditionByIdUseCase: GetConditionByIdUseCase
) : DetailsViewModel<Condition>() {
    override fun getItemById(id: Long): Condition = getConditionByIdUseCase.getById(id)
}