package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.OverviewViewModel

class AmmunitionOverviewViewModel(
    private val getAllAmmunitionsUseCase: GetAllAmmunitionsUseCase
) : OverviewViewModel<Ammunition>() {
    override fun getAllItems(): List<Ammunition> =
        getAllAmmunitionsUseCase.getAll()
}