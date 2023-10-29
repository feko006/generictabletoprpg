package com.feko.generictabletoprpg.spell

import kotlinx.coroutines.flow.Flow

interface IGetAllSpellSchools {
    fun getAllSchools(): Flow<List<String>>
}
