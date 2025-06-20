package com.feko.generictabletoprpg.common.ui.palette

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.feko.generictabletoprpg.common.ui.theme.GenerictabletoprpgTheme
import com.feko.generictabletoprpg.common.ui.components.FillingLoadingIndicator

@PreviewScreenSizes
@Preview
@Composable
fun LoadingIndicatorPreview() {
    GenerictabletoprpgTheme {
        FillingLoadingIndicator()
    }
}