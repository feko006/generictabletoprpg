package com.feko.generictabletoprpg.import

interface OrcbrewImportSpellsUseCase {
    fun import(
        sources: Map<Any, Any>
    ): Result<Boolean>
}
