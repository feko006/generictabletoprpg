package com.feko.generictabletoprpg.common.domain.usecase

interface IOrcbrewImportUseCase {
    fun import(sources: Map<Any, Any>): Result<Boolean>
}