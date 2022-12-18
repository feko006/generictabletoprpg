package com.feko.generictabletoprpg.spell

import com.feko.generictabletoprpg.common.DetailsViewModel

class SpellDetailsViewModel(
    private val getSpellByIdUseCase: GetSpellByIdUseCase
) : DetailsViewModel<Spell>() {
    override fun getItemById(id: Long): Spell = getSpellByIdUseCase.getById(id)
}