package com.feko.generictabletoprpg.armor

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class ArmorOverviewViewModel(
    private val getAllArmors: IGetAll<Armor>
) : OverviewViewModel<Armor>() {
    override fun getAllItems(): List<Armor> = getAllArmors.getAllSortedByName()
}