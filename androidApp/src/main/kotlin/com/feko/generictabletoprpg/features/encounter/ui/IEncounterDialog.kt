package com.feko.generictabletoprpg.features.encounter.ui

import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText
import com.feko.generictabletoprpg.features.encounter.InitiativeEntryEntity

sealed interface IEncounterDialog {

    data object None : IEncounterDialog

    data class InitiativeDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(R.string.initiative)
    }

    data class HealDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(R.string.heal_dialog_title)
    }

    data class DamageDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(R.string.take_damage_dialog_title)
    }

    data class RemoveAfterTakingDamageDialog(
        val entry: InitiativeEntryEntity
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(R.string.delete_dialog_title)
        val message: IText = IText.StringResourceText(
            R.string.remove_from_encounter_after_taking_lethal_damage_dialog_message_template,
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
        val title: IText = IText.StringResourceText(R.string.delete_dialog_title)
    }

    data object ConfirmResetDialog : IEncounterDialog {
        val title: IText = IText.StringResourceText(R.string.reset_dialog_title)
        val message: IText = IText.StringResourceText(R.string.reset_encounter_message)
    }

    data class PickLegendaryActionDialog(
        val entriesWithLegendaryActions: List<InitiativeEntryEntity>
    ) : IEncounterDialog {
        val title: IText = IText.StringResourceText(R.string.select_legendary_action)
    }
}