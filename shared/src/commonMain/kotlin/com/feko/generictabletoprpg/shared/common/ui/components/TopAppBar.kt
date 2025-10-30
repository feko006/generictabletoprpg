package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.common.ui.theme.ScreenSize

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GttrpgTopAppBar(
    title: IText,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title.text()) },
        navigationIcon = {
            if (LocalDimens.current.screenSize == ScreenSize.Compact) {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(menuIcon, "")
                }
            }
        },
        actions = actions
    )
}