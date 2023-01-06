package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.DetailsViewModel

class AmmunitionDetailsViewModel(
    private val getAmmunitionByIdUseCase: GetAmmunitionByIdUseCase
) : DetailsViewModel<Ammunition>() {
    override fun getItemById(id: Long): Ammunition = getAmmunitionByIdUseCase.getById(id)
}