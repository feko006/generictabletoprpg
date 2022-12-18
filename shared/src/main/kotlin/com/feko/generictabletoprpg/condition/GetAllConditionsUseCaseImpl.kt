package com.feko.generictabletoprpg.condition

class GetAllConditionsUseCaseImpl(
    private val getAllConditionsPort: GetAllConditionsPort
) : GetAllConditionsUseCase {
    override fun getAll() = getAllConditionsPort.getAllSortedByName()
}