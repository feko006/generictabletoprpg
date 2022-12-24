package com.feko.generictabletoprpg.weapon

class GetAllWeaponsUseCaseImpl(
    private val getAllWeaponsPort: GetAllWeaponsPort
) : GetAllWeaponsUseCase {
    override fun getAll() = getAllWeaponsPort.getAllSortedByName()
}