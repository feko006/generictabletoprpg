package com.feko.generictabletoprpg.common

interface OrcbrewImportUseCase {
    fun import(
        sources: Map<Any, Any>
    ): Result<Boolean>
}
