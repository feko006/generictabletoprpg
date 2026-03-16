package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.add
import com.feko.generictabletoprpg.add_temporary_hp_dialog_title
import com.feko.generictabletoprpg.delete_dialog_title
import com.feko.generictabletoprpg.equipment
import com.feko.generictabletoprpg.heal_dialog_title
import com.feko.generictabletoprpg.increase_percentage_dialog_title
import com.feko.generictabletoprpg.reduce_percentage_dialog_title
import com.feko.generictabletoprpg.refresh_all_tracked_things_dialog_title
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.tracker.model.StatsContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.skills
import com.feko.generictabletoprpg.spell_list
import com.feko.generictabletoprpg.subtract
import com.feko.generictabletoprpg.take_damage_dialog_title

sealed interface ITrackerDialog {

    @Immutable
    data object None : ITrackerDialog

    @Immutable
    data class SpellListDialog(
        val spellList: TrackedThing,
        val isFilteringByPreparedSpells: Boolean,
        val title: IText = Res.string.spell_list.asText(),
        val secondaryDialog: ISpellListDialogDialogs = ISpellListDialogDialogs.None
    ) : ITrackerDialog

    @Immutable
    data class ConfirmDeletionDialog(
        val itemToDelete: TrackedThing,
        val title: IText = Res.string.delete_dialog_title.asText()
    ) : ITrackerDialog

    @Immutable
    data class RefreshAllDialog(
        val title: IText = Res.string.refresh_all_tracked_things_dialog_title.asText()
    ) : ITrackerDialog

    @Immutable
    data class AddToPercentageDialog(
        val percentage: TrackedThing,
        val title: IText = Res.string.increase_percentage_dialog_title.asText()
    ) : ITrackerDialog

    @Immutable
    data class SubtractFromPercentageDialog(
        val percentage: TrackedThing,
        val title: IText = Res.string.reduce_percentage_dialog_title.asText()
    ) : ITrackerDialog

    @Immutable
    data class AddToNumberDialog(
        val number: TrackedThing,
        val title: IText = Res.string.add.asText()
    ) : ITrackerDialog

    @Immutable
    data class SubtractFromNumberDialog(
        val number: TrackedThing,
        val title: IText = Res.string.subtract.asText()
    ) : ITrackerDialog

    @Immutable
    data class DamageHealthDialog(
        val health: TrackedThing,
        val title: IText = Res.string.take_damage_dialog_title.asText()
    ) : ITrackerDialog

    @Immutable
    data class HealHealthDialog(
        val health: TrackedThing,
        val title: IText = Res.string.heal_dialog_title.asText()
    ) : ITrackerDialog

    @Immutable
    data class AddTemporaryHpDialog(
        val health: TrackedThing,
        val title: IText = Res.string.add_temporary_hp_dialog_title.asText()
    ) : ITrackerDialog

    @Immutable
    data class PreviewStatSkillsDialog(
        val stats: StatsContainer,
        val title: IText = Res.string.skills.asText()
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

    @Immutable
    data class EquipmentListDialog(
        val equipment: TrackedThing,
        val title: IText = Res.string.equipment.asText(),
        val secondaryDialog: IEquipmentListDialogDialogs = IEquipmentListDialogDialogs.None
    ) : ITrackerDialog
}