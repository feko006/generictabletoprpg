package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.spell.SpellRange
import com.feko.generictabletoprpg.shared.features.tracker.model.SpellListEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing

@Preview
@Composable
fun SpellListDialogPreview() {
    SpellListDialog(
        ITrackerDialog.SpellListDialog(
            spellList = TrackedThing(0, "Spell List", "", TrackedThing.Type.SpellList, 0)
                .apply {
                    serializedItem = mutableListOf(
                        getSpellListEntry1(),
                        getSpellListEntry2()
                    )
                },
            isFilteringByPreparedSpells = false
        ),
        listOf(TrackedThing.emptyOfType(TrackedThing.Type.SpellSlot, 0, 0L)),
        spellListState = rememberLazyListState(),
        onDialogDismissed = {},
        onFilteringByPreparedStateChanged = {},
        onSpellPreparedStateChanged = { _, _ -> },
        onCastSpellRequested = {},
        onRemoveSpellRequested = {},
        onSpellClick = {}
    )
}

@Preview
@Composable
fun SpellListEntryListItemPreview() {
    SpellListEntryListItem(
        getSpellListEntry2(),
        canCastSpell = true,
        onSpellPreparedStateChanged = {},
        onCastSpellClicked = {},
        onRemoveSpellRequested = {},
        onSpellClick = {}
    )
}

private fun getSpellListEntry1() = SpellListEntry(
    id = 1,
    name = "Spell 1",
    description = "",
    school = "Abjuration",
    duration = "",
    concentration = true,
    level = 0,
    source = "",
    components = Spell.SpellComponents(
        verbal = true,
        somatic = true,
        material = true,
        materialComponent = null
    ),
    castingTime = "1 action",
    classesThatCanCast = listOf(),
    range = SpellRange(
        isSelf = true,
        isTouch = true,
        isSight = true,
        distance = 0,
        unit = null
    ),
    isRitual = true,
    isPrepared = false
)

private fun getSpellListEntry2() = SpellListEntry(
    id = 2,
    name = "Spell 2",
    description = "",
    school = "Conjuration",
    duration = "",
    concentration = true,
    level = 1,
    source = "",
    components = Spell.SpellComponents(
        true,
        somatic = true,
        material = true,
        materialComponent = null
    ),
    castingTime = "1 bonus action",
    classesThatCanCast = listOf(),
    range = SpellRange(
        isSelf = true,
        isTouch = true,
        isSight = true,
        distance = 0,
        unit = null
    ),
    isRitual = true,
    isPrepared = true
)