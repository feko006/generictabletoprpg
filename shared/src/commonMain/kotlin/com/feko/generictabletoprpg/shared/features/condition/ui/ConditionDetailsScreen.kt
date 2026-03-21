package com.feko.generictabletoprpg.shared.features.condition.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.features.condition.Condition
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConditionDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<ConditionDetailsViewModel, Condition>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        Text(it.description)
    }
}