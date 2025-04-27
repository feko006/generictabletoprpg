package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface IText {
    @Composable
    fun text(): String

    data class StringResourceText(@StringRes private val stringResource: Int) : IText {
        @Composable
        override fun text(): String = stringResource(stringResource)
    }

    data class StringText(private val string: String) : IText {
        @Composable
        override fun text(): String = string
    }
}