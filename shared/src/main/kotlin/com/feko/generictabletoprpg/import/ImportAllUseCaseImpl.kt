package com.feko.generictabletoprpg.import

class ImportAllUseCaseImpl(
    private val orcbrewImportAllUseCase: OrcbrewImportAllUseCase,
    private val jsonImportAllUseCase: JsonImportAllUseCase
) : ImportAllUseCase {
    override fun import(content: String): Result<Boolean> {
        val orcbrewImportResult = orcbrewImportAllUseCase.import(content)
        if (orcbrewImportResult.isSuccess) {
            return orcbrewImportResult
        }
        val jsonImportResult = jsonImportAllUseCase.import(content)
        if (jsonImportResult.isSuccess) {
            return jsonImportResult
        }
        return Result.failure(Exception())
    }
}