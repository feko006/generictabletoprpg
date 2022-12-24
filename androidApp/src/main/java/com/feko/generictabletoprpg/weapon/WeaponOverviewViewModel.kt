package com.feko.generictabletoprpg.weapon

import com.feko.generictabletoprpg.common.OverviewViewModel

class WeaponOverviewViewModel(
    private val getAllWeaponsUseCase: GetAllWeaponsUseCase
) : OverviewViewModel<Weapon>() {
    override fun getAllItems(): List<Weapon> = getAllWeaponsUseCase.getAll()
}