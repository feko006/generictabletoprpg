package com.feko.generictabletoprpg.action

class GetAllActionsUseCaseImpl(
    private val getAllActionsPort: GetAllActionsPort
) : GetAllActionsUseCase {
    override fun getAll() = getAllActionsPort.getAllSortedByName()
}