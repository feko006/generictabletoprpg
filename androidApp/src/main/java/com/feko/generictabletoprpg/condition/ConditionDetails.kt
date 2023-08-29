package com.feko.generictabletoprpg.condition

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

object ConditionDetails : DetailsScreen<ConditionDetailsViewModel, Condition>() {
    override val screenTitle: String
        get() = "Condition Details"
    override val isRootDestination: Boolean
        get() = false
    override val routeBase: String
        get() = "conditionDetails"

    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Condition>,
        padding: Dp
    ) {
        readiedItem.item.run {
            TextWithLabel("Name", name)
            HorizontalDivider(thickness = padding)
            Text(description)
        }
    }

    @Composable
    override fun getViewModel(): ConditionDetailsViewModel = koinViewModel()
}