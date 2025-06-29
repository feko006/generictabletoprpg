package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep

@Keep
data class StatSkillEntry(
    val name: String,
    val bonus: Int,
    val passiveScore: Int,
    val additionalBonus: Int,
    val isProficient: Boolean,
    val showPassive: Boolean = false
)