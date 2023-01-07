package com.feko.generictabletoprpg.armor

class GetArmorByIdUseCaseImpl(
    private val getArmorByIdPort: GetArmorByIdPort
) : GetArmorByIdUseCase {
    override fun getById(id: Long) = getArmorByIdPort.getById(id)
}