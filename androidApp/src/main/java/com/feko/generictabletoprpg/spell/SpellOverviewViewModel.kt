package com.feko.generictabletoprpg.spell

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class SpellOverviewViewModel(
    private val getAllSpells: IGetAll<Spell>
) : OverviewViewModel<Spell>() {
    override fun getAllItems(): List<Spell> = getAllSpells.getAllSortedByName()
}