package com.feko.generictabletoprpg.import

interface OrcbrewImportAllUseCase {
    fun import(ednContent: String): Result<Boolean>
}