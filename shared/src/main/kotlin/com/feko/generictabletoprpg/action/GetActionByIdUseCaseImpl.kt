package com.feko.generictabletoprpg.action

class GetActionByIdUseCaseImpl(
    private val getActionByIdPort: GetActionByIdPort
) : GetActionByIdUseCase {
    override fun getById(id: Long) = getActionByIdPort.getById(id)
}