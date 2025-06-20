package com.feko.generictabletoprpg.common.domain.model

sealed interface IText {
    @androidx.compose.runtime.Composable
    fun text(): String

    data class StringResourceText(
        @androidx.annotation.StringRes private val stringResource: Int,
        @Suppress("ArrayInDataClass")
        private val formatArgs: Array<String> = emptyArray()
    ) : IText {
        @androidx.compose.runtime.Composable
        override fun text(): String =
            androidx.compose.ui.res.stringResource(stringResource, formatArgs)
    }

    data class StringText(private val string: String) : IText {
        @androidx.compose.runtime.Composable
        override fun text(): String = string
    }
}