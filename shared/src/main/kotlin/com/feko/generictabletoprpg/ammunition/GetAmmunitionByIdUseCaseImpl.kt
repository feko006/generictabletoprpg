package com.feko.generictabletoprpg.ammunition

class GetAmmunitionByIdUseCaseImpl(
    private val getAmmunitionByIdPort: GetAmmunitionByIdPort
) : GetAmmunitionByIdUseCase {
    override fun getById(id: Long) = getAmmunitionByIdPort.getById(id)
}