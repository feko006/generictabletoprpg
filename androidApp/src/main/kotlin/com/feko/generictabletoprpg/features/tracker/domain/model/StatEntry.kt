package com.feko.generictabletoprpg.features.tracker.domain.model

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate

@DoNotObfuscate
data class StatEntry(
    val name: String,
    val shortName: String,
    val score: Int,
    var bonus: Int,
    val savingThrowBonus: Int,
    val savingThrowAdditionalBonus: Int,
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
