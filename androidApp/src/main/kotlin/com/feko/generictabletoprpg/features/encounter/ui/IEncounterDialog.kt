package com.feko.generictabletoprpg.features.encounter.ui

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.delete_dialog_title
import com.feko.generictabletoprpg.heal_dialog_title
import com.feko.generictabletoprpg.initiative
import com.feko.generictabletoprpg.remove_from_encounter_after_taking_lethal_damage_dialog_message_template
import com.feko.generictabletoprpg.reset_dialog_title
import com.feko.generictabletoprpg.reset_encounter_message
import com.feko.generictabletoprpg.select_legendary_action
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryEntity
import com.feko.generictabletoprpg.take_damage_dialog_title

sealed interface IEncounterDialog {

    data object None : IEncounterDialog

    data class InitiativeDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(Res.string.initiative)
    }

    data class HealDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(Res.string.heal_dialog_title)
    }

    data class DamageDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(Res.string.take_damage_dialog_title)
    }

    data class RemoveAfterTakingDamageDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(Res.string.delete_dialog_title)
        val message: IText = IText.StringResourceText(
            Res.string.remove_from_encounter_after_taking_lethal_damage_dialog_message_template,
            arrayOf(entry.name)
        )
    }

    data class EditDialog(
        val entry: InitiativeEntryEntity,
        val isLairActionsButtonVisible: Boolean,
        val title: IText
    ) : IEncounterDialog

    data class ConfirmDeletionDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(Res.string.delete_dialog_title)
    }

    data object ConfirmResetDialog : IEncounterDialog {
        val title: IText = IText.StringResourceText(Res.string.reset_dialog_title)
        val message: IText = IText.StringResourceText(Res.string.reset_encounter_message)
    }

    data class PickLegendaryActionDialog(
        val entriesWithLegendaryActions: List<InitiativeEntryEntity>
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(Res.string.select_legendary_action)
    }
}