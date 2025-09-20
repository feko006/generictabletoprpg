package com.feko.generictabletoprpg.common.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

sealed interface IText {
    @Composable
    fun text(): String

    data class StringResourceText(
        @get:StringRes private val stringResource: Int,
        @Suppress("ArrayInDataClass")
        private val formatArgs: Array<String> = emptyArray()
    ) : IText {
        @Composable
        override fun text(): String =
            androidx.compose.ui.res.stringResource(stringResource, formatArgs)

        companion object {
            fun Int.asText(): IText = StringResourceText(this)
        }
    }

    data class StringText(private val string: String) : IText {
        @Composable
        override fun text(): String = string

        companion object {
            fun String.asText(): IText = StringText(this)
        }
    }
}

