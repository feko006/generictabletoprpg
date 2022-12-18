package com.feko.generictabletoprpg.spell

import com.feko.generictabletoprpg.common.OverviewViewModel

class SpellOverviewViewModel(
    private val getAllSpellsUseCase: GetAllSpellsUseCase
) : OverviewViewModel<Spell>() {
    override fun getAllItems(): List<Spell> = getAllSpellsUseCase.getAll()
}