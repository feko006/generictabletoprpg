package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import kotlinx.serialization.Serializable

@DoNotObfuscate
@Serializable
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
