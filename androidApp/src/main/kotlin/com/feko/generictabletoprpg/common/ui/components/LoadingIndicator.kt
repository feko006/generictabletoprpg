package com.feko.generictabletoprpg.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun FillingLoadingIndicator() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        LoadingIndicator(
            Modifier.size(LocalDimens.current.iconLarge),
            polygons = listOf(
                MaterialShapes.Square,
                MaterialShapes.Triangle,
                MaterialShapes.Diamond,
                MaterialShapes.Gem,
                MaterialShapes.Pentagon,
            ).shuffled()
        )
    }
}

