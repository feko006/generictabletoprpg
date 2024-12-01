package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.Stats
import kotlinx.coroutines.flow.Flow

interface IStatsEditDialogTrackerViewModel : IBaseDialogTrackerViewModel {
    val editedStats: Flow<Stats?>
    fun updateStatsName(name: String)
    fun updateStatsProficiencyBonus(proficiencyBonus: String)
    fun updateSpellSaveDcAdditionalBonus(spellSaveDcAdditionalBonus: String)
    fun updateSpellAttackAdditionalBonus(spellAttackAdditionalBonus: String)
    fun isBonusValid(bonus: Int?): Boolean
}
