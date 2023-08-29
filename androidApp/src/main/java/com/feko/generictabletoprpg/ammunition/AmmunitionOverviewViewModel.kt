package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class AmmunitionOverviewViewModel(
    private val getAllAmmunitions: IGetAll<Ammunition>
) : OverviewViewModel<Ammunition>() {
    override fun getAllItems(): List<Ammunition> =
        getAllAmmunitions.getAllSortedByName()
}