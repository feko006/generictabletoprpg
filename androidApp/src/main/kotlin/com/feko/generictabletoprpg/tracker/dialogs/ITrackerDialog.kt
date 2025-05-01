package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs.ISpellListDialogDialogs
import com.feko.generictabletoprpg.tracker.SpellList

sealed interface ITrackerDialog {
    data object None : ITrackerDialog

    data class SpellListDialog(
        val spellList: SpellList,
        val isFilteringByPreparedSpells: Boolean,
        val title: IText = IText.StringResourceText(R.string.spell_list),
        val secondaryDialog: ISpellListDialogDialogs = ISpellListDialogDialogs.None
    ) : ITrackerDialog
}