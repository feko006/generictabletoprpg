package com.feko.generictabletoprpg.armor

class GetAllArmorsUseCaseImpl(
    private val getAllArmorsPort: GetAllArmorsPort
) : GetAllArmorsUseCase {
    override fun getAll() = getAllArmorsPort.getAllSortedByName()
}