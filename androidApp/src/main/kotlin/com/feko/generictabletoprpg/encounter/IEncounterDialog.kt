package com.feko.generictabletoprpg.encounter

import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText

sealed interface IEncounterDialog {

    data object None : IEncounterDialog

    data class InitiativeDialog(
        val entry: InitiativeEntryEntity,
        val title: IText = IText.StringResourceText(R.string.initiative)
    ) : IEncounterDialog

    data class HealDialog(
        val entry: InitiativeEntryEntity,
        val title: IText = IText.StringResourceText(R.string.heal_dialog_title)
    ) : IEncounterDialog

    data class DamageDialog(
        val entry: InitiativeEntryEntity,
        val title: IText = IText.StringResourceText(R.string.take_damage_dialog_title)
    ) : IEncounterDialog

    data class RemoveAfterTakingDamageDialog(
        val entry: InitiativeEntryEntity,
        val title: IText = IText.StringResourceText(R.string.delete_dialog_title),
        val message: IText = IText.StringResourceText(
            R.string.remove_from_encounter_after_taking_lethal_damage_dialog_message_template,
            arrayOf(entry.name)
        )
    ) : IEncounterDialog

    data class EditDialog(
        val entry: InitiativeEntryEntity,
        val isLairActionsButtonVisible: Boolean,
        val title: IText
    ) : IEncounterDialog

    data class ConfirmDeletionDialog(
        val entry: InitiativeEntryEntity,
        val title: IText = IText.StringResourceText(R.string.delete_dialog_title)
    ) : IEncounterDialog
}