package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.action.InsertActionsPort
import com.feko.generictabletoprpg.app.AppModel
import com.feko.generictabletoprpg.common.Logger

class JsonImportAllUseCaseImpl(
    private val logger: Logger,
    private val jsonPort: JsonPort,
    private val insertActionsPort: InsertActionsPort
) : JsonImportAllUseCase {
    override fun import(content: String): Result<Boolean> {
        try {
            val appModel = jsonPort.from(content, AppModel::class.java)
            val results = mutableListOf<Result<Boolean>>()
            val actions = appModel.sources.flatMap { source ->
                source.actions.map {
                    it.source = source.name
                    it
                }
            }
            val actionsImported = if (actions.isEmpty()) {
                Result.success(true)
            } else {
                insertActionsPort.insertAll(actions)
            }
            results.add(actionsImported)
            return results.fold(Result.success(true)) { current, result ->
                val currentResult = current.getOrDefault(false)
                val nextResult = result.getOrDefault(false)
                Result.success(currentResult && nextResult)
            }
        } catch (e: Exception) {
            logger.error(e, "Failed to process file")
            return Result.failure(e)
        }
    }
}

