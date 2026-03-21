package com.feko.generictabletoprpg.shared.features.feat.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.requirements
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.feat.Feat
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeatDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<FeatDetailsViewModel, Feat>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        if (it.hasRequirements) {
            TextWithLabel(Res.string.requirements, it.requirements)
            HorizontalDivider()
        }
        Text(it.description)
    }
}