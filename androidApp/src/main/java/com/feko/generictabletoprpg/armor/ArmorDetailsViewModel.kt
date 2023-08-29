package com.feko.generictabletoprpg.armor

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class ArmorDetailsViewModel(
    private val getArmorById: IGetById<Armor>
) : DetailsViewModel<Armor>() {
    override fun getItemById(id: Long): Armor = getArmorById.getById(id)
}