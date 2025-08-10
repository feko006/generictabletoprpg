package com.feko.generictabletoprpg.features.tracker.ui

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText
import com.feko.generictabletoprpg.features.tracker.domain.model.StatsContainer
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThing

sealed interface ITrackerDialog {

    data object None : ITrackerDialog

    @Immutable
    data class SpellListDialog(
        val spellList: TrackedThing,
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
        val percentage: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.increase_percentage_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class SubtractFromPercentageDialog(
        val percentage: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.reduce_percentage_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class AddToNumberDialog(
        val number: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.add)
    ) : ITrackerDialog

    @Immutable
    data class SubtractFromNumberDialog(
        val number: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.subtract)
    ) : ITrackerDialog

    @Immutable
    data class DamageHealthDialog(
        val health: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.take_damage_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class HealHealthDialog(
        val health: TrackedThing,
        val title: IText = IText.StringResourceText(R.string.heal_dialog_title)
    ) : ITrackerDialog

    @Immutable
    data class AddTemporaryHpDialog(
        val health: TrackedThing,
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
        val stats: TrackedThing,
        val title: IText
    ) : ITrackerDialog
}