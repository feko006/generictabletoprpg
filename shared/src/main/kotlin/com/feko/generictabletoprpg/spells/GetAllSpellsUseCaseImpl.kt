package com.feko.generictabletoprpg.spells

class GetAllSpellsUseCaseImpl(
    private val getAllSpellsPort: GetAllSpellsPort
) : GetAllSpellsUseCase {
    override fun invoke() = getAllSpellsPort.invoke()
}