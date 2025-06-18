package com.feko.generictabletoprpg.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface IText {
    @Composable
    fun text(): String

    data class StringResourceText(
        @StringRes private val stringResource: Int,
        @Suppress("ArrayInDataClass")
        private val formatArgs: Array<String> = emptyArray()
    ) : IText {
        @Composable
        override fun text(): String = stringResource(stringResource, formatArgs)
    }

    data class StringText(private val string: String) : IText {
        @Composable
        override fun text(): String = string
    }
}