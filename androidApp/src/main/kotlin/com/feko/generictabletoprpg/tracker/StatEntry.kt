package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
data class StatEntry(
    val name: String,
    val shortName: String,
    val value: Int,
    val bonus: Int,
    val isProficientInSavingThrow: Boolean,
    val isSpellcastingModifier: Boolean,
    val skills: List<StatSkillEntry>,
    val use5ESkillBonusCalculation: Boolean = true,
)
