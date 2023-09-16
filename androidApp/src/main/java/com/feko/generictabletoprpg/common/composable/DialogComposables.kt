package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.theme.Typography

@Composable
fun DialogTitle(dialogTitle: String) {
    Text(dialogTitle, style = Typography.titleLarge)
}

@Composable
fun DialogTitle(@StringRes dialogTitleResource: Int) {
    DialogTitle(stringResource(dialogTitleResource))
}
