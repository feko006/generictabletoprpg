package com.feko.generictabletoprpg.shared.features.action.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.features.action.Action
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActionDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<ActionDetailsViewModel, Action>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        Text(it.description)
    }
}