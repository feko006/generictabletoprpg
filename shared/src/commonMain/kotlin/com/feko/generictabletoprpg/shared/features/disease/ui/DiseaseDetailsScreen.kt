package com.feko.generictabletoprpg.shared.features.disease.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.features.disease.Disease
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiseaseDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<DiseaseDetailsViewModel, Disease>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        Text(it.description)
    }
}