package com.feko.generictabletoprpg.feat

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeatDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(appBarTitle = "Feat Details", navBarActions = listOf())
    DetailsScreen<FeatDetailsViewModel, Feat>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
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