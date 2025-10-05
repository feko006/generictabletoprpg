package com.feko.generictabletoprpg.shared.features.searchall.usecase

import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.domain.model.INamed

class SearchAllUseCase(
    private val getAllDaos: List<IGetAllDao<*>>
) : ISearchAllUseCase {
    override suspend fun getAllItems(): List<Any> =
        getAllDaos
            .flatMap {
                it.getAllSortedByName().filterNotNull()
            }.sortedBy {
                (it as INamed).name
            }
}