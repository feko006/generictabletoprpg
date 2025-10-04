package com.feko.generictabletoprpg.features.feat.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.feat_details_title
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.feat.Feat
import org.koin.androidx.compose.koinViewModel

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
        TextWithLabel(R.string.name, it.name)
        if (it.hasRequirements) {
            TextWithLabel(R.string.requirements, it.requirements)
        }
        HorizontalDivider()
        Text(it.description)
    }
}