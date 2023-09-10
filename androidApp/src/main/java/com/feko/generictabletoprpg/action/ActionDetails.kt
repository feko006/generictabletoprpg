package com.feko.generictabletoprpg.action

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun ActionDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(appBarTitle = "Action Details", navBarActions = listOf())
    DetailsScreen<ActionDetailsViewModel, Action>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
            TextWithLabel("Name", name)
            HorizontalDivider(thickness = padding)
            Text(description)
        }
    }
}