package com.feko.generictabletoprpg.weapon

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class WeaponOverviewViewModel(
    private val getAllWeapons: IGetAll<Weapon>
) : OverviewViewModel<Weapon>() {
    override fun getAllItems(): List<Weapon> = getAllWeapons.getAllSortedByName()
}