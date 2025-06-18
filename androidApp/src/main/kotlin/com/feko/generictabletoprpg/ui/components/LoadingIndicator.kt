package com.feko.generictabletoprpg.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.theme.LocalDimens

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun LoadingIndicator() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        androidx.compose.material3.LoadingIndicator(Modifier.size(LocalDimens.current.iconLarge))
    }
}
