package com.feko.generictabletoprpg.shared.features.searchall.usecase

interface ISearchAllUseCase {
    suspend fun getAllItems(): List<Any>
}