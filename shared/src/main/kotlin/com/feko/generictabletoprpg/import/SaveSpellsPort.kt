package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.spells.Spell

interface SaveSpellsPort {
    fun save(spells: List<Spell>): Result<Boolean>
}