package com.feko.generictabletoprpg.import

interface OrcbrewImportAllUseCase : ImportAllUseCase

interface ImportAllUseCase {
    fun import(content: String): Result<Boolean>
}