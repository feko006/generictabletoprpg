package com.feko.generictabletoprpg.weapon

class GetWeaponByIdUseCaseImpl(
    private val getWeaponByIdPort: GetWeaponByIdPort
) : GetWeaponByIdUseCase {
    override fun getById(id: Long) = getWeaponByIdPort.getById(id)
}