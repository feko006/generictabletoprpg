package com.feko.generictabletoprpg.common

interface IOrcbrewImportUseCase {
    fun import(
        sources: Map<Any, Any>
    ): Result<Boolean>
}
