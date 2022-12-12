package com.feko.generictabletoprpg.spell

interface SaveSpellsPort {
    fun save(spells: List<Spell>): Result<Boolean>
}