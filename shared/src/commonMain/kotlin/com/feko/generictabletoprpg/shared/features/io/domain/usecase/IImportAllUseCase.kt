package com.feko.generictabletoprpg.shared.features.io.domain.usecase

interface IImportAllUseCase {
    suspend fun import(content: String): Result<Boolean>
}