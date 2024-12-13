package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.alertdialog.EmptyAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.tracker.dialogs.IStatsEditDialogSubViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object EmptyStatsEditDialogTrackerViewModel : IStatsEditDialogSubViewModel {

    override val editedStats: Flow<Stats?>
        get() = MutableStateFlow(null)

    override fun updateStatsName(name: String) = Unit
    override fun updateStatsProficiencyBonus(proficiencyBonus: String) = Unit
    override fun updateStatsInitiativeAdditionalBonus(initiativeAdditionalBonus: String) = Unit
    override fun updateSpellSaveDcAdditionalBonus(spellSaveDcAdditionalBonus: String) = Unit
    override fun updateSpellAttackAdditionalBonus(spellAttackAdditionalBonus: String) = Unit
    override fun updateStatScore(statIndex: Int, statScore: String) = Unit
    override fun updateStatSavingThrowProficiency(statIndex: Int, isProficient: Boolean) = Unit
    override fun updateStatSpellcastingModifier(statIndex: Int, isSpellcastingModifier: Boolean) =
        Unit

    override fun updateStatSkillAdditionalBonus(
        statIndex: Int,
        skillIndex: Int,
        skillAdditionalBonus: String
    ) = Unit

    override fun updateStatSkillProficiency(
        statIndex: Int,
        skillIndex: Int,
        isProficient: Boolean
    ) = Unit

    override fun updateStatSavingThrowAdditionalBonus(
        statIndex: Int,
        savingThrowAdditionalBonus: String
    ) = Unit

    override val confirmButtonEnabled: MutableStateFlow<Boolean>
        get() = MutableStateFlow(true)

    override fun confirmDialogAction() = Unit

    override val alertDialog: IAlertDialogSubViewModel = EmptyAlertDialogSubViewModel

}
