package com.feko.generictabletoprpg.weapon

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class WeaponDetailsViewModel(
    private val getWeaponById: IGetById<Weapon>
) : DetailsViewModel<Weapon>() {
    override fun getItemById(id: Long): Weapon = getWeaponById.getById(id)
}