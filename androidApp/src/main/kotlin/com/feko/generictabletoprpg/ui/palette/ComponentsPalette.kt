package com.feko.generictabletoprpg.ui.palette

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.feko.generictabletoprpg.theme.GenerictabletoprpgTheme
import com.feko.generictabletoprpg.ui.components.FillingLoadingIndicator

@PreviewScreenSizes
@Preview
@Composable
fun LoadingIndicatorPreview() {
    GenerictabletoprpgTheme {
        FillingLoadingIndicator()
    }
}