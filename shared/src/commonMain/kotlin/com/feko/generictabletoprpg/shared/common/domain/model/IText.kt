package com.feko.generictabletoprpg.shared.common.domain.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface IText {
    @Composable
    fun text(): String

    data class StringResourceText(
        private val stringResource: StringResource,
        @Suppress("ArrayInDataClass")
        private val formatArgs: Array<String> = emptyArray()
    ) : IText {
        @Composable
        override fun text(): String =
            stringResource(stringResource, formatArgs)

        companion object {
            fun StringResource.asText(): IText = StringResourceText(this)
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