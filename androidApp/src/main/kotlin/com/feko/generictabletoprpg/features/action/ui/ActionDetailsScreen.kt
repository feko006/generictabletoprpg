package com.feko.generictabletoprpg.features.action.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.features.action.Action
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun ActionDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.action_details_title),
        navBarActions = listOf()
    )
    DetailsScreen<ActionDetailsViewModel, Action>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
            TextWithLabel(R.string.name, name)
            HorizontalDivider(thickness = padding)
            Text(description)
        }
    }
}