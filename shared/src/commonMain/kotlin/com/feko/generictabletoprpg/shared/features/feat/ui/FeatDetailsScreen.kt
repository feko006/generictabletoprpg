package com.feko.generictabletoprpg.shared.features.feat.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.feat_details_title
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.requirements
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
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
        Res.string.feat_details_title.asText(),
        onNavigationIconClick
    ) {
        TextWithLabel(Res.string.name, it.name)
        if (it.hasRequirements) {
            TextWithLabel(Res.string.requirements, it.requirements)
        }
        HorizontalDivider()
        Text(it.description)
    }
}