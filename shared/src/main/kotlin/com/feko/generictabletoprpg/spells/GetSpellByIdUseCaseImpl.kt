package com.feko.generictabletoprpg.spells

class GetSpellByIdUseCaseImpl(
    private val getSpellByIdPort: GetSpellByIdPort
) : GetSpellByIdUseCase {
    override fun invoke(id: Long) = getSpellByIdPort.invoke(id)
}