package com.feko.generictabletoprpg.spell

interface GetAllSpellsPort {
    fun getAllSortedByName(): List<Spell>
}
