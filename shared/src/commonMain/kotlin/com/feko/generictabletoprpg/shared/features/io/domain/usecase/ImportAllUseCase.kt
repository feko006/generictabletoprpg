package com.feko.generictabletoprpg.shared.features.io.domain.usecase

class ImportAllUseCase(
    private val orcbrewImportAllUseCase: IOrcbrewImportAllUseCase,
    private val jsonImportAllUseCase: IJsonImportAllUseCase
) : IImportAllUseCase {
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