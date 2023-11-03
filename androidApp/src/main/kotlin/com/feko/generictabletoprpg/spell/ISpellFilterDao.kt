package com.feko.generictabletoprpg.spell

import kotlinx.coroutines.flow.Flow

interface ISpellFilterDao {
    fun getAllLevels(): Flow<List<Int>>
    fun getAllSchools(): Flow<List<String>>
}