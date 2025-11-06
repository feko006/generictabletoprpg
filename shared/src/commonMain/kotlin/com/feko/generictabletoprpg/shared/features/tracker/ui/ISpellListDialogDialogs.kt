package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.confirm_spell_removal_from_list_dialog_title
import com.feko.generictabletoprpg.select_slot_level_for_casting_spell
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.features.tracker.model.SpellListEntry

sealed interface ISpellListDialogDialogs {

    @Immutable
    data object None : ISpellListDialogDialogs

    @Immutable
    data class SelectSpellSlotDialog(
        val availableSlots: List<Int>,
        val title: IText = IText.StringResourceText(Res.string.select_slot_level_for_casting_spell)
    ) : ISpellListDialogDialogs

    @Immutable
    data class ConfirmSpellRemovalDialog(
        val spellListEntry: SpellListEntry,
        val title: IText = IText.StringResourceText(Res.string.confirm_spell_removal_from_list_dialog_title)
    ) : ISpellListDialogDialogs
}