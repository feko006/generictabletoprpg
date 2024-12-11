package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
data class StatEntry(
    val name: String,
    val shortName: String,
    val score: Int,
    var bonus: Int,
    val isProficientInSavingThrow: Boolean,
    val isSpellcastingModifier: Boolean,
    val skills: List<StatSkillEntry>,
    val use5ESkillBonusCalculation: Boolean = true,
) {
    init {
        if (use5ESkillBonusCalculation) {
            bonus = calculateBonus(score)
        }
    }

    private fun calculateBonus(score: Int): Int = score / 2 - 5
}
