package com.feko.generictabletoprpg.shared.features.encounter.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryEntity

@Preview
@Composable
fun InitiativeListItemPreview() {
    InitiativeListItem(
        InitiativeEntryEntity(1, "Larry", 10, 18, 19, 3, 1, 14, 7, false, false, false),
        onUpdateInitiativeRequested = {},
        onUpdateKeepOnReset = {},
        onHealButtonClicked = {},
        onDamageButtonClicked = {},
        onDuplicateButtonClicked = {},
        onEditButtonClicked = {},
        onDeleteButtonClicked = {}
    )
}