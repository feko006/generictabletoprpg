package com.feko.generictabletoprpg.shared.common.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val isBigScreen: Boolean,
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val visualLarge: Dp,
    val gapSmall: Dp,
    val gapMedium: Dp,
)

fun scaledDimens(factor: Float, isBigScreen: Boolean): Dimens =
    Dimens(
        isBigScreen = isBigScreen,
        paddingSmall = (8 * factor).dp,
        paddingMedium = (16 * factor).dp,
        visualLarge = (100 * factor).dp,
        gapSmall = (8 * factor).dp,
        gapMedium = (16 * factor).dp,
    )

val compactDimens = scaledDimens(1f, isBigScreen = false)
val mediumDimens = scaledDimens(1.25f, isBigScreen = true)
val expandedDimens = scaledDimens(1.5f, isBigScreen = true)
