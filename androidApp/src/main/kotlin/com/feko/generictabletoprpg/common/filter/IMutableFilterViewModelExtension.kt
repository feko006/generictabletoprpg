package com.feko.generictabletoprpg.common.filter

interface IMutableFilterViewModelExtension : IFilterViewModelExtension {
    suspend fun setFilterButtonVisible(isVisible: Boolean)
}