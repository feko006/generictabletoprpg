package com.feko.generictabletoprpg.tracker.dialogs

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs.ISpellListDialogDialogs
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.Number
import com.feko.generictabletoprpg.tracker.Percentage
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.Stats
import com.feko.generictabletoprpg.tracker.StatsContainer
import com.feko.generictabletoprpg.tracker.TrackedThing

sealed interface ITrackerDialog {

    data object None : ITrackerDialog

    @Immutable
    data class SpellListDialog(
        val spellList: SpellList,
        val isFilteringByPreparedSpells: Boolean,
        val title: IText = IText.StringResourceText(R.string.spell_list),
        val secondaryDialog: ISpellListDialogDialogs = ISpellListDialogDialogs.None
    ) : ITrackerDialog

    @Immutable
    data class ConfirmDeletionDialog(
        val itemToDelete: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.delete_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class RefreshAllDialog(
        val title: IText = IText.StringResourceText(R.string.refresh_all_tracked_things_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class AddToPercentageDialog(
        val percentage: Percentage,
        val title: IText = IText.StringResourceText(R.string.increase_percentage_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class SubtractFromPercentageDialog(
        val percentage: Percentage,
        val title: IText = IText.StringResourceText(R.string.reduce_percentage_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class AddToNumberDialog(
        val number: Number,
        val title: IText = IText.StringResourceText(R.string.add)
    ) : ITrackerDialog

    @Immutable
    data class SubtractFromNumberDialog(
        val number: Number,
        val title: IText = IText.StringResourceText(R.string.subtract)
    ) : ITrackerDialog

    @Immutable
    data class DamageHealthDialog(
        val health: Health,
        val title: IText = IText.StringResourceText(R.string.take_damage_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class HealHealthDialog(
        val health: Health,
        val title: IText = IText.StringResourceText(R.string.heal_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class AddTemporaryHpDialog(
        val health: Health,
        val title: IText = IText.StringResourceText(R.string.add_temporary_hp_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class PreviewStatSkillsDialog(
        val stats: StatsContainer,
        val title: IText = IText.StringResourceText(R.string.skills)
    ) : ITrackerDialog

    @Immutable
    data class EditDialog(
        val editedItem: TrackedThing,
        val title: IText
    ) : ITrackerDialog

    @Immutable
    data class StatsEditDialog(
        val stats: Stats,
        val title: IText
    ) : ITrackerDialog
}