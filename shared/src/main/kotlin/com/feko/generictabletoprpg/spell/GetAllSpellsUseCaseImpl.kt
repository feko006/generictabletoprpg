package com.feko.generictabletoprpg.spell

class GetAllSpellsUseCaseImpl(
    private val getAllSpellsPort: GetAllSpellsPort
) : GetAllSpellsUseCase {
    override fun getAll() = getAllSpellsPort.getAllSortedByName()
}