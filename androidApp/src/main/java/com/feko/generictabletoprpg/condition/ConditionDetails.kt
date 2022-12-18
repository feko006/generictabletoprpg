package com.feko.generictabletoprpg.condition

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            Divider(Modifier.padding(vertical = padding))
            Text(description)
        }
    }

    @Composable
    override fun getViewModel(): ConditionDetailsViewModel = koinViewModel()
}