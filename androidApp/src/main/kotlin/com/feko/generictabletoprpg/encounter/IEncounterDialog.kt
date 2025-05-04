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
}