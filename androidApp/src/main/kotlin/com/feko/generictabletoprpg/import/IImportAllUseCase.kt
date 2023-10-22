package com.feko.generictabletoprpg.import

interface IImportAllUseCase {
    fun import(content: String): Result<Boolean>
}