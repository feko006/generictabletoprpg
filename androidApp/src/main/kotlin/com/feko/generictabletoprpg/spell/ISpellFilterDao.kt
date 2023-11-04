package com.feko.generictabletoprpg.spell

import kotlinx.coroutines.flow.Flow

interface ISpellFilterDao {
    fun getAllSchools(): Flow<List<String>>
    fun getAllLevels(): Flow<List<Int>>
    fun getAllClasses(): Flow<List<String>>
}