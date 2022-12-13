package com.feko.generictabletoprpg.spell

class GetSpellByIdUseCaseImpl(
    private val getSpellByIdPort: GetSpellByIdPort
) : GetSpellByIdUseCase {
    override fun getById(id: Long) = getSpellByIdPort.getById(id)
}