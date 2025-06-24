package com.feko.generictabletoprpg.common.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val visualLarge: Dp,
    val gapSmall: Dp
)

fun scaledDimens(factor: Float): Dimens =
    Dimens(
        paddingSmall = (8 * factor).dp,
        paddingMedium = (16 * factor).dp,
        visualLarge = (100 * factor).dp,
        gapSmall = (8 * factor).dp
    )

val compactDimens = scaledDimens(1f)
val mediumDimens = scaledDimens(1.25f)
val expandedDimens = scaledDimens(1.5f)
