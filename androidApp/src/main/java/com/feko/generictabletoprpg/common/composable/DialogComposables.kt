package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.theme.Typography

@Composable
fun DialogTitle(dialogTitle: String) {
    Text(dialogTitle, style = Typography.titleLarge)
}
