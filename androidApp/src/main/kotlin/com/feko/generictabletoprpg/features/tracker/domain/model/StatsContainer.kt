package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep

@Keep
data class StatsContainer(
    val proficiencyBonus: Int,
    val spellSaveDc: Int,
    val spellSaveDcAdditionalBonus: Int,
    val spellAttackBonus: Int,
    val spellAttackAdditionalBonus: Int,
    val initiative: Int,
    val initiativeAdditionalBonus: Int,
    val stats: List<StatEntry>,
    val use5eCalculations: Boolean = true
) {
    companion object {
        val Empty = StatsContainer(0, 0, 0, 0, 0, 0, 0, emptyList())
    }
}