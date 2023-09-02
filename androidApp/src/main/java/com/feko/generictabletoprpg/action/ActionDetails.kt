package com.feko.generictabletoprpg.action

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

object ActionDetails
    : DetailsScreen<ActionDetailsViewModel, Action>(
    "Action Details",
    "action"
) {
    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Action>,
        padding: Dp
    ) {
        readiedItem.item.run {
            TextWithLabel("Name", name)
            HorizontalDivider(thickness = padding)
            Text(description)
        }
    }

    @Composable
    override fun getViewModel(): ActionDetailsViewModel = koinViewModel()
}