package com.feko.generictabletoprpg.shared.features.searchall.usecase

import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SearchAllUseCase(
    private val getAllDaos: List<IGetAllDao<*>>
) : ISearchAllUseCase {
    override fun getAllItems(): Flow<List<Any>> =
        combine(
            flows = getAllDaos.map { it.getAllSortedByName() },
            transform = { it.toList().flatten().filterNotNull() })
            .map { allItems -> allItems.sortedBy { (it as INamed).name } }
}