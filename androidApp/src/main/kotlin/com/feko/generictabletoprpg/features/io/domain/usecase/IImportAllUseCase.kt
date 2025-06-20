package com.feko.generictabletoprpg.features.io.domain.usecase

interface IImportAllUseCase {
    fun import(content: String): Result<Boolean>
}