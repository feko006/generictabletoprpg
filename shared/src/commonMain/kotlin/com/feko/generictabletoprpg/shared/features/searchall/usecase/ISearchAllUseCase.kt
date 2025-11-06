package com.feko.generictabletoprpg.shared.features.searchall.usecase

import kotlinx.coroutines.flow.Flow

interface ISearchAllUseCase {
    fun getAllItems(): Flow<List<Any>>
}