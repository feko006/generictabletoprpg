package com.feko.generictabletoprpg.import

interface ImportAllUseCase {
    fun import(content: String): Result<Boolean>
}