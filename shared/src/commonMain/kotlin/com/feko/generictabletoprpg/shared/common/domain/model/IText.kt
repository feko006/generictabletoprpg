package com.feko.generictabletoprpg.shared.common.domain.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface IText {
    @Composable
    fun text(): String

    data class StringResourceText(
        private val stringResource: StringResource,
        private val formatArgs: Collection<IText> = emptyList()
    ) : IText {
        @Composable
        override fun text(): String =
            stringResource(stringResource, *formatArgs.map { it.text() }.toTypedArray())

        companion object {
            fun StringResource.asText(): IText = StringResourceText(this)
            fun StringResource.asText(formatArgs: Collection<IText>): IText =
                StringResourceText(this, formatArgs)
        }
    }

    data class StringText(
        private val string: String,
        private val formatArgs: Collection<IText> = emptyList()
    ) : IText {
        @Composable
        override fun text(): String = string.format(*formatArgs.map { it.text() }.toTypedArray())

        companion object {
            fun String.asText(): IText = StringText(this)
            fun String.asText(formatArgs: Collection<IText>): IText = StringText(this, formatArgs)
        }
    }
}