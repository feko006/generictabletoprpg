package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
data class StatsContainer(
    val proficiencyBonus: Int,
    val spellSaveDc: Int,
    val spellSaveDcAdditionalBonus: Int,
    val spellAttackBonus: Int,
    val spellAttackAdditionalBonus: Int,
    val stats: List<StatEntry>,
    val use5eCalculations: Boolean = true
)