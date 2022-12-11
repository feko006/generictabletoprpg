package com.feko.generictabletoprpg.spell

class GetSpellByIdUseCaseImpl(
    private val getSpellByIdPort: GetSpellByIdPort
) : GetSpellByIdUseCase {
    override fun invoke(id: Long) = getSpellByIdPort.getById(id)
}