package com.feko.generictabletoprpg.init

import com.feko.generictabletoprpg.common.IUserPreferences
import com.feko.generictabletoprpg.import.IJsonImportAllUseCase
import com.feko.generictabletoprpg.import.IOrcbrewImportAllUseCase

class LoadBaseContentUseCase(
    private val userPreferences: IUserPreferences,
    private val loadBaseContentPort: ILoadBaseContent,
    private val orcbrewImportAllUseCase: IOrcbrewImportAllUseCase,
    private val jsonImportAllUseCase: IJsonImportAllUseCase
) : ILoadBaseContentUseCase {
    override fun invoke() {
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
        const val LOADED_BASE_CONTENT_VERSION_KEY = "loaded_base_content_version_key"
        const val INVALID_BASE_CONTENT_VERSION = -1
        const val CURRENT_BASE_CONTENT_VERSION = 7
    }
}