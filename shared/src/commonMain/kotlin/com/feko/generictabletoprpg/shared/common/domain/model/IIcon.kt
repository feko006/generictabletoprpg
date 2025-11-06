package com.feko.generictabletoprpg.shared.common.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

sealed interface IIcon {
    @Composable
    fun imageVector(): ImageVector

    data class ImageVectorIcon(private val imageVector: ImageVector) : IIcon {
        @Composable
        override fun imageVector(): ImageVector = imageVector

        companion object {
            fun ImageVector.asIcon(): IIcon = ImageVectorIcon(this)
        }
    }

    data class DrawableResourceIcon(private val drawableResource: DrawableResource) : IIcon {
        @Composable
        override fun imageVector(): ImageVector = vectorResource(drawableResource)

        companion object {
            fun DrawableResource.asIcon(): IIcon = DrawableResourceIcon(this)
        }
    }
}