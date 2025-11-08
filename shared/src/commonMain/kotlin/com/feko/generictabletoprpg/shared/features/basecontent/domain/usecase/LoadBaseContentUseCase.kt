package com.feko.generictabletoprpg.shared.features.basecontent.domain.usecase

import com.feko.generictabletoprpg.shared.common.domain.IUserPreferences
import com.feko.generictabletoprpg.shared.features.basecontent.domain.ILoadBaseContent
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IJsonImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IOrcbrewImportAllUseCase

class LoadBaseContentUseCase(
    private val userPreferences: IUserPreferences,
    private val loadBaseContentPort: ILoadBaseContent,
    private val orcbrewImportAllUseCase: IOrcbrewImportAllUseCase,
    private val jsonImportAllUseCase: IJsonImportAllUseCase
) : ILoadBaseContentUseCase {
    override suspend fun invoke() {
        val latestBaseContentLoaded =
            userPreferences.getIntOrDefault(
                LOADED_BASE_CONTENT_VERSION_KEY,
                INVALID_BASE_CONTENT_VERSION
            )

        if (latestBaseContentLoaded >= CURRENT_BASE_CONTENT_VERSION) {
            return
        }

        val allResults = mutableListOf<Result<Boolean>>()

        val baseOrcbrewContent = loadBaseContentPort.loadOrcbrewBaseContent()
        val orcbrewResult = orcbrewImportAllUseCase.import(baseOrcbrewContent)
        allResults.add(orcbrewResult)

        val baseJsonContent = loadBaseContentPort.loadJsonBaseContent()
        val jsonResult = jsonImportAllUseCase.import(baseJsonContent)
        allResults.add(jsonResult)

        val result = allResults.fold(Result.success(true)) { current, result ->
            val currentResult = current.getOrDefault(false)
            val nextResult = result.getOrDefault(false)
            Result.success(currentResult && nextResult)
        }

        if (!result.getOrDefault(false)) {
            throw IllegalStateException(
                "App must initialize successfully on start",
                result.exceptionOrNull()
            )
        }

        userPreferences.setInt(
            LOADED_BASE_CONTENT_VERSION_KEY,
            CURRENT_BASE_CONTENT_VERSION
        )
    }

    companion object {
        private const val LOADED_BASE_CONTENT_VERSION_KEY = "loaded_base_content_version_key"
        private const val INVALID_BASE_CONTENT_VERSION = -1
        private const val CURRENT_BASE_CONTENT_VERSION = 8
    }
}