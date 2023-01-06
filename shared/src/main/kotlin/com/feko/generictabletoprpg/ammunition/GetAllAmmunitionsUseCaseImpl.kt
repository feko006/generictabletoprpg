package com.feko.generictabletoprpg.ammunition

class GetAllAmmunitionsUseCaseImpl(
    private val getAllAmmunitionsPort: GetAllAmmunitionsPort
) : GetAllAmmunitionsUseCase {
    override fun getAll() = getAllAmmunitionsPort.getAllSortedByName()
}