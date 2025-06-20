package com.feko.generictabletoprpg.common.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val iconLarge: Dp
)

fun scaledDimens(factor: Float): Dimens =
    Dimens(
        iconLarge = (100 * factor).dp
    )

val compactDimens = scaledDimens(1f)
val mediumDimens = scaledDimens(1.25f)
val expandedDimens = scaledDimens(1.5f)
