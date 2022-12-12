package com.feko.generictabletoprpg.import

interface OrcbrewImportFeatsUseCase {
    fun import(
        sources: Map<Any, Any>
    ): Result<Boolean>
}
