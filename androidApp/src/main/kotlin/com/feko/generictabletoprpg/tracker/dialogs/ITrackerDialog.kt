package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs.ISpellListDialogDialogs
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.TrackedThing

sealed interface ITrackerDialog {
    data object None : ITrackerDialog

    data class SpellListDialog(
        val spellList: SpellList,
        val isFilteringByPreparedSpells: Boolean,
        val title: IText = IText.StringResourceText(R.string.spell_list),
        val secondaryDialog: ISpellListDialogDialogs = ISpellListDialogDialogs.None
    ) : ITrackerDialog

    data class ConfirmDeletionDialog(
        val itemToDelete: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.delete_dialog_title)
    ) : ITrackerDialog

    data class RefreshAllDialog(
        val title: IText = IText.StringResourceText(R.string.refresh_all_tracked_things_dialog_title)
    ) : ITrackerDialog
}