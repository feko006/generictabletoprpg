package com.feko.generictabletoprpg.shared.common.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val screenSize: ScreenSize,
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val visualLarge: Dp,
    val gapSmall: Dp,
    val gapMedium: Dp,
)

enum class ScreenSize {
    Compact,
    Medium,
    Expanded
}

fun scaledDimens(factor: Float, screenSize: ScreenSize): Dimens =
    Dimens(
        screenSize = screenSize,
        paddingSmall = (8 * factor).dp,
        paddingMedium = (16 * factor).dp,
        visualLarge = (100 * factor).dp,
        gapSmall = (8 * factor).dp,
        gapMedium = (16 * factor).dp,
    )

val compactDimens = scaledDimens(1f, ScreenSize.Compact)
val mediumDimens = scaledDimens(1.25f, ScreenSize.Medium)
val expandedDimens = scaledDimens(1.5f, ScreenSize.Expanded)
