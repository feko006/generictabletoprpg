package com.feko.generictabletoprpg.spell

interface GetSpellByIdPort {
    fun getById(spellId: Long): Spell
}