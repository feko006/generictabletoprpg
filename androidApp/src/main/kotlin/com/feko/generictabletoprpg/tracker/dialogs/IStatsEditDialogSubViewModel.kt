package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.common.alertdialog.IStatefulAlertDialogSubViewModel
import com.feko.generictabletoprpg.tracker.Stats

interface IStatsEditDialogSubViewModel {
    val alertDialog: IStatefulAlertDialogSubViewModel<Stats>

    fun updateStatsName(name: String)
    fun updateStatsProficiencyBonus(proficiencyBonus: Int)
    fun updateStatsInitiativeAdditionalBonus(initiativeAdditionalBonus: Int)
    fun updateSpellSaveDcAdditionalBonus(spellSaveDcAdditionalBonus: Int)
    fun updateSpellAttackAdditionalBonus(spellAttackAdditionalBonus: Int)
    fun updateStatScore(statIndex: Int, statScore: Int)
    fun updateStatSavingThrowProficiency(statIndex: Int, isProficient: Boolean)
    fun updateStatSpellcastingModifier(statIndex: Int, isSpellcastingModifier: Boolean)
    fun updateStatSkillAdditionalBonus(
        statIndex: Int,
        skillIndex: Int,
        skillAdditionalBonus: Int
    )

    fun updateStatSkillProficiency(statIndex: Int, skillIndex: Int, isProficient: Boolean)
    fun updateStatSavingThrowAdditionalBonus(statIndex: Int, savingThrowAdditionalBonus: Int)
}
