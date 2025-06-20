package com.feko.generictabletoprpg.common.ui.viewmodel

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class ButtonState(
    val icon: ImageVector? = null,
    val painter: Painter? = null,
    val onClick: () -> Unit
)