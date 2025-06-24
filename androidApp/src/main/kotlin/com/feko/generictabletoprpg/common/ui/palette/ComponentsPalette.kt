package com.feko.generictabletoprpg.common.ui.palette

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.feko.generictabletoprpg.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.common.ui.components.FillingLoadingIndicator
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.components.SearchTextField
import com.feko.generictabletoprpg.common.ui.theme.GttrpgTheme

@PreviewScreenSizes
@Preview
@Composable
fun LoadingIndicatorPreview() {
    GttrpgTheme {
        FillingLoadingIndicator()
    }
}

@PreviewScreenSizes
@Preview
@Composable
fun TopAppBarPreview() {
    GttrpgTheme {
        GttrpgTopAppBar("Top App Bar".asText(), {}) {
            IconButton({}) {
                Icon(Icons.Default.Diamond, "")
            }
        }
    }
}

@PreviewScreenSizes
@Preview
@Composable
fun SearchTextFieldPreview(
    @PreviewParameter(provider = SearchTextProvider::class)
    searchString: String
) {
    GttrpgTheme {
        SearchTextField(searchString, {})
    }
}

class SearchTextProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("Search String", "")

}
