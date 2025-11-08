package com.feko.generictabletoprpg.shared.features.action.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.action_details_title
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
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
        Res.string.action_details_title.asText(),
        onNavigationIconClick
    ) {
        TextWithLabel(Res.string.name, it.name)
        HorizontalDivider()
        Text(it.description)
    }
}