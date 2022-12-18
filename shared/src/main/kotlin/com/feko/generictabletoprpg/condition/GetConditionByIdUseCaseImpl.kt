package com.feko.generictabletoprpg.condition

class GetConditionByIdUseCaseImpl(
    private val getConditionByIdPort: GetConditionByIdPort
) : GetConditionByIdUseCase {
    override fun getById(id: Long) = getConditionByIdPort.getById(id)
}