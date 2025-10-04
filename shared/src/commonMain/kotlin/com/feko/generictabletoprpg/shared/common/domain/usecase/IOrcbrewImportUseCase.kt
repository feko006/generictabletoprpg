package com.feko.generictabletoprpg.shared.common.domain.usecase

interface IOrcbrewImportUseCase {
    fun import(sources: Map<Any, Any>): Result<Boolean>
}