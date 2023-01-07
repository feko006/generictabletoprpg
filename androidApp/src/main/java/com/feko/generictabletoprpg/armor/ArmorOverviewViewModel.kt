package com.feko.generictabletoprpg.armor

import com.feko.generictabletoprpg.common.OverviewViewModel

class ArmorOverviewViewModel(
    private val getAllArmorsUseCase: GetAllArmorsUseCase
) : OverviewViewModel<Armor>() {
    override fun getAllItems(): List<Armor> = getAllArmorsUseCase.getAll()
}