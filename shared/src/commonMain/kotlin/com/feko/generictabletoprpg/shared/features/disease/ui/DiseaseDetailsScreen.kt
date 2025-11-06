package com.feko.generictabletoprpg.shared.features.disease.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.disease_details_title
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
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
        Res.string.disease_details_title.asText(),
        onNavigationIconClick
    ) {
        TextWithLabel(Res.string.name, it.name)
        HorizontalDivider()
        Text(it.description)
    }
}