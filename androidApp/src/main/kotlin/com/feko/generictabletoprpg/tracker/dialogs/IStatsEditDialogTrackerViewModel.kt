package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.Stats
import kotlinx.coroutines.flow.Flow

interface IStatsEditDialogTrackerViewModel : IBaseDialogTrackerViewModel {
    val editedStats: Flow<Stats?>
    fun updateStatsName(name: String)
    fun updateStatsProficiencyBonus(proficiencyBonus: String)
    fun updateSpellSaveDcAdditionalBonus(spellSaveDcAdditionalBonus: String)
    fun updateSpellAttackAdditionalBonus(spellAttackAdditionalBonus: String)
    fun updateStatScore(statIndex: Int, statScore: String)
    fun updateStatSavingThrowProficiency(statIndex: Int, isProficient: Boolean)
    fun updateStatSpellcastingModifier(statIndex: Int, isSpellcastingModifier: Boolean)
    fun updateStatSkillAdditionalBonus(
        statIndex: Int,
        skillIndex: Int,
        skillAdditionalBonus: String
    )

    fun updateStatSkillProficiency(statIndex: Int, skillIndex: Int, isProficient: Boolean)
}
