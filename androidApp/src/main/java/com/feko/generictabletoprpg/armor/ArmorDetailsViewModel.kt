package com.feko.generictabletoprpg.armor

import com.feko.generictabletoprpg.common.DetailsViewModel

class ArmorDetailsViewModel(
    private val getArmorByIdUseCase: GetArmorByIdUseCase
) : DetailsViewModel<Armor>() {
    override fun getItemById(id: Long): Armor = getArmorByIdUseCase.getById(id)
}