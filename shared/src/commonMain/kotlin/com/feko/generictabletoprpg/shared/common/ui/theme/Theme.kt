package com.feko.generictabletoprpg.shared.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.window.core.layout.WindowSizeClass

val LocalDimens = staticCompositionLocalOf<Dimens> { error("No Dimens provided") }

@Composable
expect fun getColorScheme(dynamicColor: Boolean, darkTheme: Boolean): ColorScheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GttrpgTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = getColorScheme(dynamicColor, darkTheme)

    val dimens =
        currentWindowAdaptiveInfo().windowSizeClass.let {
            when {
                it.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> expandedDimens
                it.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> mediumDimens
                else -> compactDimens
            }
        }

    CompositionLocalProvider(LocalDimens provides dimens) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

fun columnCount(uiWidth: Dp): Int = when {
    uiWidth.value >= WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND -> 3
    uiWidth.value >= WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND -> 2
    else -> 1
}