package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
data class StatSkillEntry(
    val name: String,
    val bonus: Int,
    val passiveScore: Int,
    val additionalBonus: Int,
    val isProficient: Boolean,
    val showPassive: Boolean = false
)