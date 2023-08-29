package com.feko.generictabletoprpg.feat

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

object FeatDetails : DetailsScreen<FeatDetailsViewModel, Feat>() {
    override val routeBase = "featDetails"
    override val screenTitle: String
        get() = "Feat Details"
    override val isRootDestination: Boolean
        get() = false

    @Composable
    override fun getViewModel(): FeatDetailsViewModel = koinViewModel()

    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Feat>,
        padding: Dp
    ) {
        readiedItem.item.run {
            TextWithLabel("Name", name)
            if (hasRequirements) {
                Spacer(Modifier.height(padding))
                TextWithLabel("Requirements", requirements)
            }
            HorizontalDivider(thickness = padding)
            Text(description)
        }
    }
}