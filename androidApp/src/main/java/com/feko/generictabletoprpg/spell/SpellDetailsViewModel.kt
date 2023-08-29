package com.feko.generictabletoprpg.spell

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class SpellDetailsViewModel(
    private val getSpellById: IGetById<Spell>
) : DetailsViewModel<Spell>() {
    override fun getItemById(id: Long): Spell = getSpellById.getById(id)
}