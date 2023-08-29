package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class AmmunitionDetailsViewModel(
    private val getAmmunitionById: IGetById<Ammunition>
) : DetailsViewModel<Ammunition>() {
    override fun getItemById(id: Long): Ammunition = getAmmunitionById.getById(id)
}