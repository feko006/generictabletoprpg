package com.feko.generictabletoprpg.features.tracker.ui

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellListEntry

sealed interface ISpellListDialogDialogs {
    data object None : ISpellListDialogDialogs

    @Immutable
    data class SelectSpellSlotDialog(
        val availableSlots: List<Int>,
        val title: IText = IText.StringResourceText(R.string.select_slot_level_for_casting_spell)
    ) : ISpellListDialogDialogs

    @Immutable
    data class ConfirmSpellRemovalDialog(
        val spellListEntry: SpellListEntry,
        val title: IText = IText.StringResourceText(R.string.confirm_spell_removal_from_list_dialog_title)
    ) : ISpellListDialogDialogs
}