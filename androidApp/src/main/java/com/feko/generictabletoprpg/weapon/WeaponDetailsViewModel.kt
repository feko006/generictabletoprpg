package com.feko.generictabletoprpg.weapon

import com.feko.generictabletoprpg.common.DetailsViewModel

class WeaponDetailsViewModel(
    private val getWeaponByIdUseCase: GetWeaponByIdUseCase
) : DetailsViewModel<Weapon>() {
    override fun getItemById(id: Long): Weapon = getWeaponByIdUseCase.getById(id)
}