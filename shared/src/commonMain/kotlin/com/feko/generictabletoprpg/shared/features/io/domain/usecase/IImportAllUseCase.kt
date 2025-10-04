package com.feko.generictabletoprpg.shared.features.io.domain.usecase

interface IImportAllUseCase {
    fun import(content: String): Result<Boolean>
}