package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.spell.Spell

interface SaveSpellsPort {
    fun save(spells: List<Spell>): Result<Boolean>
}